package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.BookingEntity
import com.example.data.db.ChatMessageEntity
import com.example.data.db.ManagerSlotEntity
import com.example.data.db.MatchPostEntity
import com.example.data.db.NotificationEntity
import com.example.data.db.PitchEntity
import com.example.data.db.PlayerStatsEntity
import com.example.data.repository.TakwiraRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TakwiraUiState(
    val selectedCity: String = "Toutes",
    val selectedPitchType: String = "Tous",
    val selectedSurface: String = "Toutes",
    val searchQuery: String = "",
    val selectedPitchForBooking: PitchEntity? = null,
    val selectedBookingDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val selectedBookingSlot: String = "19:00 - 20:00",
    val paymentMethod: String = "D17", // "D17", "Konnect", "Sobflous", "Carte Bancaire", "Sur Place"
    val isPaymentProcessing: Boolean = false,
    val paymentSuccessBooking: BookingEntity? = null,
    val smsToastMessage: String? = null,
    val isManagerMode: Boolean = false,
    val isDarkMode: Boolean = true, // Dark mode default for sleek sports look
    val isSyncingCloud: Boolean = false,
    val lastCloudSyncTime: String = "Aujourd'hui à 03:36",
    val activeTab: Int = 0, // 0: Explore, 1: Chat/Matches, 2: Stats, 3: Manager, 4: Profil
    val notificationFilter: String = "TOUT" // "TOUT", "SMS", "PUSH"
)

class TakwiraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TakwiraRepository = TakwiraRepository(
        AppDatabase.getDatabase(application)
    )

    private val _uiState = MutableStateFlow(TakwiraUiState())
    val uiState: StateFlow<TakwiraUiState> = _uiState

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }
    }

    val pitches: StateFlow<List<PitchEntity>> = combine(
        repository.allPitches,
        _uiState
    ) { pitchList, state ->
        pitchList.filter { pitch ->
            val matchesCity = state.selectedCity == "Toutes" || pitch.city.equals(state.selectedCity, ignoreCase = true)
            val matchesType = state.selectedPitchType == "Tous" || pitch.pitchType.equals(state.selectedPitchType, ignoreCase = true)
            val matchesSurface = when (state.selectedSurface) {
                "Toutes" -> true
                "Synthétique" -> pitch.surface.contains("Synthétique", ignoreCase = true)
                "Indoor" -> pitch.isIndoor || pitch.surface.contains("Indoor", ignoreCase = true)
                "Gazon Naturel" -> pitch.surface.contains("Gazon", ignoreCase = true)
                else -> true
            }
            val matchesSearch = state.searchQuery.isBlank() ||
                    pitch.name.contains(state.searchQuery, ignoreCase = true) ||
                    pitch.city.contains(state.searchQuery, ignoreCase = true) ||
                    pitch.address.contains(state.searchQuery, ignoreCase = true)

            matchesCity && matchesType && matchesSurface && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val bookings: StateFlow<List<BookingEntity>> = repository.allBookings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val matchPosts: StateFlow<List<MatchPostEntity>> = repository.allMatchPosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val playerStats: StateFlow<PlayerStatsEntity?> = repository.playerStats.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.getChatMessages("General").stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectTab(index: Int) {
        _uiState.update { it.copy(activeTab = index) }
    }

    fun setCityFilter(city: String) {
        _uiState.update { it.copy(selectedCity = city) }
    }

    fun setPitchTypeFilter(type: String) {
        _uiState.update { it.copy(selectedPitchType = type) }
    }

    fun setSurfaceFilter(surface: String) {
        _uiState.update { it.copy(selectedSurface = surface) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun openBookingForPitch(pitch: PitchEntity) {
        _uiState.update {
            it.copy(
                selectedPitchForBooking = pitch,
                selectedBookingSlot = "19:00 - 20:00"
            )
        }
    }

    fun closeBookingDialog() {
        _uiState.update {
            it.copy(
                selectedPitchForBooking = null,
                paymentSuccessBooking = null,
                isPaymentProcessing = false
            )
        }
    }

    fun setBookingDate(date: String) {
        _uiState.update { it.copy(selectedBookingDate = date) }
    }

    fun setBookingSlot(slot: String) {
        _uiState.update { it.copy(selectedBookingSlot = slot) }
    }

    fun setPaymentMethod(method: String) {
        _uiState.update { it.copy(paymentMethod = method) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun toggleManagerMode() {
        _uiState.update { it.copy(isManagerMode = !it.isManagerMode) }
    }

    fun processPaymentAndConfirmBooking(
        userName: String,
        userPhone: String
    ) {
        val state = _uiState.value
        val pitch = state.selectedPitchForBooking ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isPaymentProcessing = true) }
            delay(1500) // Simulate online payment gateway delay (Konnect / D17 / Sobflous)

            val booking = repository.confirmBooking(
                pitchId = pitch.id,
                pitchName = pitch.name,
                city = pitch.city,
                date = state.selectedBookingDate,
                timeSlot = state.selectedBookingSlot,
                priceTnd = pitch.pricePerHour,
                paymentMethod = state.paymentMethod,
                userName = userName,
                userPhone = userPhone
            )

            val smsMsg = "SMS Takwira TN: Votre réservation pour ${pitch.name} le ${state.selectedBookingDate} à ${state.selectedBookingSlot} est validée! Code: #${booking.confirmationCode}"

            _uiState.update {
                it.copy(
                    isPaymentProcessing = false,
                    paymentSuccessBooking = booking,
                    smsToastMessage = smsMsg
                )
            }
        }
    }

    fun dismissSmsToast() {
        _uiState.update { it.copy(smsToastMessage = null) }
    }

    fun sendChat(messageText: String) {
        if (messageText.isBlank()) return
        val player = playerStats.value
        val name = player?.name ?: "Joueur Takwira"

        viewModelScope.launch {
            repository.sendChatMessage(
                group = "General",
                senderName = name,
                role = "Joueur",
                text = messageText,
                isMine = true
            )
        }
    }

    fun createMatchPost(
        title: String,
        pitchName: String,
        city: String,
        dateTime: String,
        requiredPlayers: Int,
        organizerName: String,
        organizerPhone: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.createMatchPost(
                title = title,
                pitchName = pitchName,
                city = city,
                dateTime = dateTime,
                requiredPlayers = requiredPlayers,
                organizerName = organizerName,
                organizerPhone = organizerPhone,
                notes = notes
            )
        }
    }

    fun joinMatch(postId: Long) {
        viewModelScope.launch {
            repository.joinMatchPost(postId)
        }
    }

    fun updateProfile(
        name: String,
        phone: String,
        favoriteClub: String,
        position: String
    ) {
        val current = playerStats.value ?: return
        viewModelScope.launch {
            val updated = current.copy(
                name = name,
                phone = phone,
                favoriteClub = favoriteClub,
                position = position
            )
            repository.updatePlayerStats(updated)
        }
    }

    fun triggerCloudBackup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncingCloud = true) }
            delay(1800) // Simulate secure cloud database sync
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            _uiState.update {
                it.copy(
                    isSyncingCloud = false,
                    lastCloudSyncTime = "Aujourd'hui à $time"
                )
            }
        }
    }

    fun markNotifRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }
}
