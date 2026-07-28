package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PitchDao {
    @Query("SELECT * FROM pitches ORDER BY id ASC")
    fun getAllPitches(): Flow<List<PitchEntity>>

    @Query("SELECT * FROM pitches WHERE city = :city ORDER BY id ASC")
    fun getPitchesByCity(city: String): Flow<List<PitchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPitches(pitches: List<PitchEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPitch(pitch: PitchEntity)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE pitchId = :pitchId ORDER BY date DESC, timeSlot ASC")
    fun getBookingsForPitch(pitchId: Long): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBooking(id: Long)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages WHERE matchGroup = :matchGroup ORDER BY id ASC")
    fun getMessagesForGroup(matchGroup: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)
}

@Dao
interface MatchPostDao {
    @Query("SELECT * FROM match_posts ORDER BY id DESC")
    fun getAllMatchPosts(): Flow<List<MatchPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchPost(post: MatchPostEntity)

    @Update
    suspend fun updateMatchPost(post: MatchPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchPosts(posts: List<MatchPostEntity>)
}

@Dao
interface PlayerStatsDao {
    @Query("SELECT * FROM player_stats WHERE id = 1 LIMIT 1")
    fun getPlayerStats(): Flow<PlayerStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: PlayerStatsEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}

@Dao
interface ManagerSlotDao {
    @Query("SELECT * FROM manager_slots WHERE pitchId = :pitchId AND date = :date")
    fun getSlotsForPitchAndDate(pitchId: Long, date: String): Flow<List<ManagerSlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlots(slots: List<ManagerSlotEntity>)

    @Update
    suspend fun updateSlot(slot: ManagerSlotEntity)
}
