package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.BookingEntity
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ManagerSlotEntity
import com.example.data.db.MatchPostEntity
import com.example.data.db.NotificationEntity
import com.example.data.db.PitchEntity
import com.example.data.db.PlayerStatsEntity
import com.example.data.sample.InitialData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TakwiraRepository(private val db: AppDatabase) {

    val allPitches: Flow<List<PitchEntity>> = db.pitchDao().getAllPitches()
    val allBookings: Flow<List<BookingEntity>> = db.bookingDao().getAllBookings()
    val allMatchPosts: Flow<List<MatchPostEntity>> = db.matchPostDao().getAllMatchPosts()
    val playerStats: Flow<PlayerStatsEntity?> = db.playerStatsDao().getPlayerStats()
    val allNotifications: Flow<List<NotificationEntity>> = db.notificationDao().getAllNotifications()

    fun getPitchesByCity(city: String): Flow<List<PitchEntity>> {
        return if (city == "Toutes" || city.isBlank()) {
            db.pitchDao().getAllPitches()
        } else {
            db.pitchDao().getPitchesByCity(city)
        }
    }

    fun getChatMessages(group: String = "General"): Flow<List<ChatMessageEntity>> {
        return db.chatDao().getMessagesForGroup(group)
    }

    fun getSlotsForPitchAndDate(pitchId: Long, date: String): Flow<List<ManagerSlotEntity>> {
        return db.managerSlotDao().getSlotsForPitchAndDate(pitchId, date)
    }

    suspend fun seedDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val existingPitches = db.pitchDao().getAllPitches().first()
        if (existingPitches.isEmpty()) {
            db.pitchDao().insertPitches(InitialData.SAMPLE_PITCHES)
            db.playerStatsDao().insertOrUpdateStats(InitialData.INITIAL_PLAYER_STATS)
            db.matchPostDao().insertMatchPosts(InitialData.SAMPLE_MATCH_POSTS)
            db.chatDao().insertMessages(InitialData.SAMPLE_CHAT_MESSAGES)
            for (notif in InitialData.SAMPLE_NOTIFICATIONS) {
                db.notificationDao().insertNotification(notif)
            }

            // Create initial manager slots for today
            val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val timeSlots = listOf(
                "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00",
                "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00",
                "22:00 - 23:00", "23:00 - 00:00"
            )
            val managerSlots = mutableListOf<ManagerSlotEntity>()
            for (pitch in InitialData.SAMPLE_PITCHES) {
                for ((index, slot) in timeSlots.withIndex()) {
                    managerSlots.add(
                        ManagerSlotEntity(
                            pitchId = pitch.id,
                            date = todayDate,
                            timeSlot = slot,
                            isBooked = index == 3 || index == 4, // 19:00 and 20:00 reserved as demo
                            isBlockedByManager = index == 7,
                            bookedByName = if (index == 3) "Kharroubi Ahmed" else if (index == 4) "Youssef Msakni" else null,
                            bookedByPhone = if (index == 3) "+216 52 111 222" else if (index == 4) "+216 98 765 432" else null
                        )
                    )
                }
            }
            db.managerSlotDao().insertSlots(managerSlots)
        }
    }

    suspend fun confirmBooking(
        pitchId: Long,
        pitchName: String,
        city: String,
        date: String,
        timeSlot: String,
        priceTnd: Double,
        paymentMethod: String,
        userName: String,
        userPhone: String
    ): BookingEntity = withContext(Dispatchers.IO) {
        val code = "TKW-" + (1000..9999).random()
        val booking = BookingEntity(
            pitchId = pitchId,
            pitchName = pitchName,
            city = city,
            date = date,
            timeSlot = timeSlot,
            priceTnd = priceTnd,
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod == "Sur Place") "AVANCE_PAYÉE" else "PAYÉ",
            confirmationCode = code,
            userName = userName,
            userPhone = userPhone
        )
        val bookingId = db.bookingDao().insertBooking(booking)

        // Generate SMS notification
        val smsNotif = NotificationEntity(
            title = "Réservation SMS Confirmée (#$code)",
            body = "Takwira TN: Votre terrain '$pitchName' ($timeSlot le $date) est confirmé. Paiement: $paymentMethod ($priceTnd TND). Code de vérification: $code.",
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            type = "SMS",
            isRead = false
        )
        db.notificationDao().insertNotification(smsNotif)

        // Generate Push notification
        val pushNotif = NotificationEntity(
            title = "Alerte Réservation Takwira",
            body = "Match programmé pour le $date à $timeSlot. Pensez à partager l'invitation avec vos coéquipiers!",
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            type = "PUSH",
            isRead = false
        )
        db.notificationDao().insertNotification(pushNotif)

        // Update player stats (add match)
        val currentStats = db.playerStatsDao().getPlayerStats().first()
            ?: InitialData.INITIAL_PLAYER_STATS
        val updatedStats = currentStats.copy(
            matchesPlayed = currentStats.matchesPlayed + 1
        )
        db.playerStatsDao().insertOrUpdateStats(updatedStats)

        booking.copy(id = bookingId)
    }

    suspend fun sendChatMessage(group: String, senderName: String, role: String, text: String, isMine: Boolean) {
        withContext(Dispatchers.IO) {
            val msg = ChatMessageEntity(
                matchGroup = group,
                senderName = senderName,
                senderRole = role,
                message = text,
                timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                isMine = isMine
            )
            db.chatDao().insertMessage(msg)
        }
    }

    suspend fun createMatchPost(
        title: String,
        pitchName: String,
        city: String,
        dateTime: String,
        requiredPlayers: Int,
        organizerName: String,
        organizerPhone: String,
        notes: String
    ) = withContext(Dispatchers.IO) {
        val post = MatchPostEntity(
            title = title,
            pitchName = pitchName,
            city = city,
            dateTime = dateTime,
            requiredPlayers = requiredPlayers,
            currentPlayers = 1,
            organizerName = organizerName,
            organizerPhone = organizerPhone,
            notes = notes,
            isFull = false
        )
        db.matchPostDao().insertMatchPost(post)
    }

    suspend fun joinMatchPost(postId: Long) = withContext(Dispatchers.IO) {
        val posts = db.matchPostDao().getAllMatchPosts().first()
        val post = posts.find { it.id == postId } ?: return@withContext
        val newCurrent = post.currentPlayers + 1
        val updated = post.copy(
            currentPlayers = newCurrent,
            isFull = newCurrent >= (post.currentPlayers + post.requiredPlayers)
        )
        db.matchPostDao().updateMatchPost(updated)

        // Push alert notification
        val pushNotif = NotificationEntity(
            title = "Nouveau Joueur Rejoint!",
            body = "Vous avez rejoint la Takwira '${post.title}'. Discutez avec l'organisateur au ${post.organizerPhone}.",
            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            type = "PUSH",
            isRead = false
        )
        db.notificationDao().insertNotification(pushNotif)
    }

    suspend fun updatePlayerStats(stats: PlayerStatsEntity) = withContext(Dispatchers.IO) {
        db.playerStatsDao().insertOrUpdateStats(stats)
    }

    suspend fun markNotificationAsRead(id: Long) = withContext(Dispatchers.IO) {
        db.notificationDao().markAsRead(id)
    }

    suspend fun addManagerSlot(slot: ManagerSlotEntity) = withContext(Dispatchers.IO) {
        db.managerSlotDao().insertSlots(listOf(slot))
    }

    suspend fun toggleManagerSlotBlock(slot: ManagerSlotEntity) = withContext(Dispatchers.IO) {
        val updated = slot.copy(isBlockedByManager = !slot.isBlockedByManager)
        db.managerSlotDao().updateSlot(updated)
    }
}
