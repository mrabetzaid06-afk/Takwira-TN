package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pitches")
data class PitchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val city: String,
    val address: String,
    val pitchType: String, // "5v5", "7v7", "11v11"
    val surface: String, // "Synthétique FIFA 2★", "Gazon Naturel", "Couvert Indoor"
    val pricePerHour: Double, // in TND
    val rating: Double,
    val amenities: String, // Comma separated: "Éclairage,Vestiaires,Douches,Cafétéria,Parking,Chrono"
    val managerPhone: String,
    val isIndoor: Boolean = false,
    val imageUrl: String = ""
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pitchId: Long,
    val pitchName: String,
    val city: String,
    val date: String, // YYYY-MM-DD
    val timeSlot: String, // e.g. "19:00 - 20:00"
    val priceTnd: Double,
    val paymentMethod: String, // "D17", "Konnect", "Sobflous", "Carte Bancaire", "Sur Place"
    val paymentStatus: String, // "PAYÉ", "AVANCE_PAYÉE", "EN_ATTENTE"
    val confirmationCode: String,
    val userName: String,
    val userPhone: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val matchGroup: String, // e.g. "General", "Match Lac 2", "Kantaoui 5v5"
    val senderName: String,
    val senderRole: String, // "Capitaine", "Joueur", "Gérant"
    val message: String,
    val timestamp: String,
    val isMine: Boolean
)

@Entity(tableName = "match_posts")
data class MatchPostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val pitchName: String,
    val city: String,
    val dateTime: String,
    val requiredPlayers: Int,
    val currentPlayers: Int,
    val organizerName: String,
    val organizerPhone: String,
    val notes: String,
    val isFull: Boolean = false
)

@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey val id: Long = 1, // Single profile for local player
    val name: String,
    val phone: String,
    val favoriteClub: String,
    val position: String, // "Attaquant", "Milieu", "Défenseur", "Gardien"
    val matchesPlayed: Int,
    val goals: Int,
    val assists: Int,
    val mvps: Int,
    val winRatePercent: Int,
    val fairPlayRating: Float,
    val badges: String // Comma separated badges
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val timestamp: String,
    val type: String, // "SMS", "PUSH", "RAPPEL"
    val isRead: Boolean = false
)

@Entity(tableName = "manager_slots")
data class ManagerSlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pitchId: Long,
    val date: String,
    val timeSlot: String,
    val isBooked: Boolean,
    val isBlockedByManager: Boolean = false,
    val bookedByName: String? = null,
    val bookedByPhone: String? = null
)
