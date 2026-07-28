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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.db.ChatMessageEntity
import com.example.data.db.MatchPostEntity
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TunisiaRed

@Composable
fun ChatCommunityScreen(
    messages: List<ChatMessageEntity>,
    matchPosts: List<MatchPostEntity>,
    onSendMessage: (String) -> Unit,
    onCreateMatchPost: (title: String, pitchName: String, city: String, dateTime: String, required: Int, organizerName: String, organizerPhone: String, notes: String) -> Unit,
    onJoinMatch: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf(0) } // 0: Chat Salon, 1: Cherche Joueurs Board
    var chatText by remember { mutableStateOf("") }
    var showCreatePostDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Tab Header
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp)
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = { Text("Chat Groupe Match", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("chat_tab_button")
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = { Text("Cherche Joueurs (${matchPosts.size})", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.GroupAdd, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("matchmaking_tab_button")
            )
        }

        if (activeSubTab == 0) {
            // Chat Room
            Column(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages) { msg ->
                        ChatMessageItem(msg)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chat Input Field
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = chatText,
                        onValueChange = { chatText = it },
                        placeholder = { Text("Écrivez un message...") },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            onSendMessage(chatText)
                            chatText = ""
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(GreenPrimary)
                            .testTag("send_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Envoyer",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        } else {
            // Cherche Joueurs Board
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Annonces de Matchs",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                    Button(
                        onClick = { showCreatePostDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier.testTag("create_match_post_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Publier Match", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(matchPosts) { post ->
                        MatchPostCard(
                            post = post,
                            onJoinClick = { onJoinMatch(post.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showCreatePostDialog) {
        CreateMatchPostDialog(
            onCreatePost = { title, pitchName, city, dateTime, required, organizerName, organizerPhone, notes ->
                onCreateMatchPost(title, pitchName, city, dateTime, required, organizerName, organizerPhone, notes)
                showCreatePostDialog = false
            },
            onDismiss = { showCreatePostDialog = false }
        )
    }
}

@Composable
fun ChatMessageItem(msg: ChatMessageEntity) {
    val isMine = msg.isMine
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        if (!isMine) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isMine) GreenPrimary else MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!isMine) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = msg.senderName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = msg.senderRole,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Text(
                    text = msg.message,
                    fontSize = 13.sp,
                    color = if (isMine) Color.White else MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = msg.timestamp,
                    fontSize = 9.sp,
                    color = if (isMine) Color.White.copy(0.7f) else MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun MatchPostCard(
    post: MatchPostEntity,
    onJoinClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${post.pitchName} • ${post.city}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (post.isFull) Color.Gray else GoldAccent)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (post.isFull) "COMPLET" else "${post.currentPlayers}/${post.currentPlayers + post.requiredPlayers} Joueurs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Horaire: ${post.dateTime}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = GreenPrimary)
            Text(text = post.notes, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.8f))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Organisé par: ${post.organizerName} (${post.organizerPhone})",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )

                Button(
                    onClick = onJoinClick,
                    enabled = !post.isFull,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    modifier = Modifier.testTag("join_match_button_${post.id}")
                ) {
                    Text(if (post.isFull) "Complet" else "Rejoindre le Match", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun CreateMatchPostDialog(
    onCreatePost: (title: String, pitchName: String, city: String, dateTime: String, required: Int, organizerName: String, organizerPhone: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("Match 5v5 ce soir - Cherche Joueurs") }
    var pitchName by remember { mutableStateOf("Complexe Sportif El Menzah") }
    var city by remember { mutableStateOf("Tunis") }
    var dateTime by remember { mutableStateOf("Ce soir 20:00 - 21:00") }
    var requiredCount by remember { mutableStateOf("2") }
    var organizerName by remember { mutableStateOf("Youssef Msakni") }
    var organizerPhone by remember { mutableStateOf("+216 98 765 432") }
    var notes by remember { mutableStateOf("Niveau sympa, amenez vos chaussures de terrain synthétique!") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Créer une Annonce Match", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre de l'Annonce") }, singleLine = true)
                OutlinedTextField(value = pitchName, onValueChange = { pitchName = it }, label = { Text("Nom du Terrain") }, singleLine = true)
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("Gouvernorat") }, singleLine = true)
                OutlinedTextField(value = dateTime, onValueChange = { dateTime = it }, label = { Text("Date & Heure") }, singleLine = true)
                OutlinedTextField(value = requiredCount, onValueChange = { requiredCount = it }, label = { Text("Joueurs Manquants") }, singleLine = true)
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Remarques / Ambiance") })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCreatePost(
                        title, pitchName, city, dateTime,
                        requiredCount.toIntOrNull() ?: 2,
                        organizerName, organizerPhone, notes
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Publier")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
