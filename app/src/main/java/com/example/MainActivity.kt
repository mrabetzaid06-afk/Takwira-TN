package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SmsPushBanner
import com.example.ui.screens.ChatCommunityScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.ManagerDashboardScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PaymentBookingDialog
import com.example.ui.screens.PlayerStatsScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TakwiraTheme
import com.example.ui.theme.TunisiaRed
import com.example.viewmodel.TakwiraViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TakwiraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val pitches by viewModel.pitches.collectAsState()
            val bookings by viewModel.bookings.collectAsState()
            val matchPosts by viewModel.matchPosts.collectAsState()
            val playerStats by viewModel.playerStats.collectAsState()
            val notifications by viewModel.notifications.collectAsState()
            val chatMessages by viewModel.chatMessages.collectAsState()

            var showNotificationsView by remember { mutableStateOf(false) }

            TakwiraTheme(darkTheme = uiState.isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TakwiraTopBar(
                                isManagerMode = uiState.isManagerMode,
                                unreadNotifCount = notifications.count { !it.isRead },
                                onOpenNotifications = { showNotificationsView = true },
                                onToggleManagerMode = { viewModel.toggleManagerMode() }
                            )
                        },
                        bottomBar = {
                            TakwiraBottomNavigation(
                                selectedTab = uiState.activeTab,
                                isManagerMode = uiState.isManagerMode,
                                onTabSelected = {
                                    showNotificationsView = false
                                    viewModel.selectTab(it)
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                // Real-time SMS Confirmation Toast/Banner
                                SmsPushBanner(
                                    message = uiState.smsToastMessage,
                                    onDismiss = { viewModel.dismissSmsToast() }
                                )

                                if (showNotificationsView) {
                                    NotificationsScreen(
                                        notifications = notifications,
                                        onMarkAsRead = { viewModel.markNotifRead(it) }
                                    )
                                } else {
                                    Crossfade(targetState = uiState.activeTab, label = "tab_crossfade") { tab ->
                                        when (tab) {
                                            0 -> ExploreScreen(
                                                pitches = pitches,
                                                selectedCity = uiState.selectedCity,
                                                selectedType = uiState.selectedPitchType,
                                                selectedSurface = uiState.selectedSurface,
                                                searchQuery = uiState.searchQuery,
                                                onCitySelected = { viewModel.setCityFilter(it) },
                                                onTypeSelected = { viewModel.setPitchTypeFilter(it) },
                                                onSurfaceSelected = { viewModel.setSurfaceFilter(it) },
                                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                                onBookPitch = { viewModel.openBookingForPitch(it) }
                                            )
                                            1 -> ChatCommunityScreen(
                                                messages = chatMessages,
                                                matchPosts = matchPosts,
                                                onSendMessage = { viewModel.sendChat(it) },
                                                onCreateMatchPost = { title, pitchName, city, dateTime, required, orgName, orgPhone, notes ->
                                                    viewModel.createMatchPost(title, pitchName, city, dateTime, required, orgName, orgPhone, notes)
                                                },
                                                onJoinMatch = { viewModel.joinMatch(it) }
                                            )
                                            2 -> ManagerDashboardScreen(
                                                pitches = pitches,
                                                bookings = bookings,
                                                isManagerMode = uiState.isManagerMode,
                                                onToggleManagerMode = { viewModel.toggleManagerMode() }
                                            )
                                            3 -> PlayerStatsScreen(
                                                stats = playerStats
                                            )
                                            4 -> ProfileSettingsScreen(
                                                playerStats = playerStats,
                                                isDarkMode = uiState.isDarkMode,
                                                isSyncingCloud = uiState.isSyncingCloud,
                                                lastCloudSyncTime = uiState.lastCloudSyncTime,
                                                onToggleDarkMode = { viewModel.toggleDarkMode() },
                                                onUpdateProfile = { name, phone, club, pos ->
                                                    viewModel.updateProfile(name, phone, club, pos)
                                                },
                                                onTriggerCloudSync = { viewModel.triggerCloudBackup() }
                                            )
                                        }
                                    }
                                }
                            }

                            // Payment Booking Modal Dialog
                            uiState.selectedPitchForBooking?.let { pitch ->
                                PaymentBookingDialog(
                                    pitch = pitch,
                                    selectedDate = uiState.selectedBookingDate,
                                    selectedSlot = uiState.selectedBookingSlot,
                                    paymentMethod = uiState.paymentMethod,
                                    isProcessing = uiState.isPaymentProcessing,
                                    successBooking = uiState.paymentSuccessBooking,
                                    onDateSelected = { viewModel.setBookingDate(it) },
                                    onSlotSelected = { viewModel.setBookingSlot(it) },
                                    onPaymentMethodSelected = { viewModel.setPaymentMethod(it) },
                                    onConfirmBooking = { name, phone ->
                                        viewModel.processPaymentAndConfirmBooking(name, phone)
                                    },
                                    onDismiss = { viewModel.closeBookingDialog() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakwiraTopBar(
    isManagerMode: Boolean,
    unreadNotifCount: Int,
    onOpenNotifications: () -> Unit,
    onToggleManagerMode: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = "Logo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Takwira TN",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(TunisiaRed)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🇹🇳 TN",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = if (isManagerMode) "Espace Gérant Complexe" else "Terrains & Réservations",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Notification Bell with Badge
            IconButton(
                onClick = onOpenNotifications,
                modifier = Modifier.testTag("notifications_top_bell")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifCount > 0) {
                            Badge(containerColor = TunisiaRed) {
                                Text("$unreadNotifCount", color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications SMS / Push",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}

@Composable
fun TakwiraBottomNavigation(
    selectedTab: Int,
    isManagerMode: Boolean,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar")
    ) {
        val navItems = listOf(
            Triple(0, "Terrains", Icons.Default.SportsSoccer),
            Triple(1, "Chat", Icons.Default.Chat),
            Triple(2, "Gérant", Icons.Default.CalendarMonth),
            Triple(3, "Stats", Icons.Default.EmojiEvents),
            Triple(4, "Profil", Icons.Default.Person)
        )

        navItems.forEach { (index, label, icon) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.testTag("nav_tab_${label.lowercase()}")
            )
        }
    }
}
