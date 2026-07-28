package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.db.PlayerStatsEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TunisiaRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    playerStats: PlayerStatsEntity?,
    isDarkMode: Boolean,
    isSyncingCloud: Boolean,
    lastCloudSyncTime: String,
    onToggleDarkMode: () -> Unit,
    onUpdateProfile: (name: String, phone: String, favoriteClub: String, position: String) -> Unit,
    onTriggerCloudSync: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(playerStats) { mutableStateOf(playerStats?.name ?: "Youssef Msakni") }
    var phone by remember(playerStats) { mutableStateOf(playerStats?.phone ?: "+216 98 765 432") }
    var favoriteClub by remember(playerStats) { mutableStateOf(playerStats?.favoriteClub ?: "Espérance de Tunis") }
    var position by remember(playerStats) { mutableStateOf(playerStats?.position ?: "Attaquant") }

    var expandedClubDropdown by remember { mutableStateOf(false) }
    var expandedPosDropdown by remember { mutableStateOf(false) }

    var smsNotifEnabled by remember { mutableStateOf(true) }
    var pushNotifEnabled by remember { mutableStateOf(true) }

    val tunisianClubs = listOf(
        "Espérance de Tunis",
        "Club Africain",
        "Étoile du Sahel",
        "CS Sfaxien",
        "CA Bizertin",
        "Stade Tunisien",
        "US Monastir",
        "Autre / Supporter neutre"
    )

    val positions = listOf(
        "Attaquant",
        "Milieu Offensif",
        "Milieu Défensif",
        "Défenseur Central",
        "Lateral / Piston",
        "Gardien de But"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header Title
        item {
            Text(
                text = "Profil Joueur & Personnalisation",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Profile Details Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(GreenPrimary)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Détails du Compte", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Modifier vos informations de joueur", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nom & Prénom") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Téléphone (+216 pour SMS)") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_phone_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Favorite Club Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedClubDropdown,
                        onExpandedChange = { expandedClubDropdown = !expandedClubDropdown }
                    ) {
                        OutlinedTextField(
                            value = favoriteClub,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Club Cœur Tunisie") },
                            leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, tint = TunisiaRed) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedClubDropdown) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedClubDropdown,
                            onDismissRequest = { expandedClubDropdown = false }
                        ) {
                            for (club in tunisianClubs) {
                                DropdownMenuItem(
                                    text = { Text(club) },
                                    onClick = {
                                        favoriteClub = club
                                        expandedClubDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Position Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedPosDropdown,
                        onExpandedChange = { expandedPosDropdown = !expandedPosDropdown }
                    ) {
                        OutlinedTextField(
                            value = position,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Poste sur le Terrain") },
                            leadingIcon = { Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = GreenPrimary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPosDropdown) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPosDropdown,
                            onDismissRequest = { expandedPosDropdown = false }
                        ) {
                            for (pos in positions) {
                                DropdownMenuItem(
                                    text = { Text(pos) },
                                    onClick = {
                                        position = pos
                                        expandedPosDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onUpdateProfile(name, phone, favoriteClub, position) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_profile_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mettre à jour le Profil", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Appearance & Dark Mode Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Mode Sombre", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Améliore le confort visuel nocturne", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                        }
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onToggleDarkMode() },
                        modifier = Modifier.testTag("dark_mode_switch")
                    )
                }
            }
        }

        // Cloud Auto-Backup & Sync Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSyncingCloud) Icons.Default.CloudSync else Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sauvegarde Cloud Automatique",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Dernière synchro: $lastCloudSyncTime",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.85f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onTriggerCloudSync,
                        enabled = !isSyncingCloud,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("force_cloud_sync_button")
                    ) {
                        if (isSyncingCloud) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sauvegarde en cours...")
                        } else {
                            Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Synchroniser les Données Maints")
                        }
                    }
                }
            }
        }

        // Notifications Preferences Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Notifications Personnalisables", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Alertes SMS de confirmation", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Recevez le code de réservation par SMS", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                        }
                        Switch(checked = smsNotifEnabled, onCheckedChange = { smsNotifEnabled = it })
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Notifications Push Dernière Minute", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Alertes joueurs manquants & invitations", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.6f))
                        }
                        Switch(checked = pushNotifEnabled, onCheckedChange = { pushNotifEnabled = it })
                    }
                }
            }
        }

        // Multi-device Continuity Info
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Smartphone, contentDescription = null, tint = GreenPrimary)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Synchronisation Multiplateforme Active: Smartphone • Tablette • Web",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
