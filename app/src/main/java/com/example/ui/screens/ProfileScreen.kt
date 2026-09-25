package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeItem
import com.example.data.model.SlotRegistration
import com.example.data.model.Tournament
import com.example.data.model.UserProfile
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlowingButton
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldRank
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToxicGreen
import com.example.ui.viewmodel.AppScreen

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    registrations: List<SlotRegistration>,
    tournaments: List<Tournament>,
    onUpdateProfile: (String, String, String) -> Unit,
    onNavigateTo: (AppScreen, String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var isEditing by remember { mutableStateOf(false) }
    var editIgn by remember { mutableStateOf(userProfile.ign) }
    var editUid by remember { mutableStateOf(userProfile.uid) }
    var editPhone by remember { mutableStateOf(userProfile.phone) }

    val myRegistrations = registrations.filter { it.ign == userProfile.ign || it.freeFireUid == userProfile.uid }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Profile Card Header
            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    borderColor = FireBluePrimary.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Avatar
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(listOf(FireBlueDark, FireBluePrimary))
                                        )
                                        .border(2.dp, BorderGlow, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "👑", fontSize = 28.sp)
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column {
                                    Text(
                                        text = userProfile.ign,
                                        color = TextPrimary,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "UID: ${userProfile.uid}",
                                        color = SkyBlueAccent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(GoldRank.copy(alpha = 0.2f))
                                            .border(0.5.dp, GoldRank, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "⭐ ${userProfile.rank}",
                                            color = GoldRank,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            IconButton(
                                onClick = { isEditing = !isEditing },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(CardNavyElevated)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = FireBluePrimary, modifier = Modifier.size(18.dp))
                            }
                        }

                        // Inline Edit Mode
                        if (isEditing) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = BorderSubtle)
                            Spacer(modifier = Modifier.height(10.dp))

                            InputField(
                                label = "In-Game Name (IGN)",
                                value = editIgn,
                                onValueChange = { editIgn = it },
                                placeholder = "IGN",
                                testTag = "profile_edit_ign"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InputField(
                                label = "Free Fire Player UID",
                                value = editUid,
                                onValueChange = { editUid = it.filter { c -> c.isDigit() } },
                                placeholder = "UID",
                                testTag = "profile_edit_uid"
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InputField(
                                label = "Phone Number",
                                value = editPhone,
                                onValueChange = { editPhone = it },
                                placeholder = "Phone",
                                testTag = "profile_edit_phone"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            GlowingButton(
                                text = "Save Profile Changes",
                                onClick = {
                                    onUpdateProfile(editIgn, editUid, editPhone)
                                    isEditing = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                testTag = "btn_save_profile"
                            )
                        }
                    }
                }
            }

            // Stats Grid
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "📊 Career eSports Statistics",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProfileStatCard(label = "Matches", value = userProfile.matchesPlayed.toString(), modifier = Modifier.weight(1f))
                        ProfileStatCard(label = "Booyahs", value = userProfile.totalWins.toString(), modifier = Modifier.weight(1f), highlightColor = GoldRank)
                        ProfileStatCard(
                            label = "Win Rate",
                            value = "${((userProfile.totalWins.toFloat() / userProfile.matchesPlayed.coerceAtLeast(1)) * 100).toInt()}%",
                            modifier = Modifier.weight(1f),
                            highlightColor = ToxicGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProfileStatCard(label = "Total Kills", value = userProfile.totalKills.toString(), modifier = Modifier.weight(1f), highlightColor = FlameOrange)
                        ProfileStatCard(label = "K/D Ratio", value = String.format("%.2f", userProfile.kdRatio), modifier = Modifier.weight(1f))
                        ProfileStatCard(label = "Headshots", value = "${userProfile.headshotRate}%", modifier = Modifier.weight(1f), highlightColor = FireBluePrimary)
                    }
                }
            }

            // Badges System
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎖️ Tournament Badges & Honors",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(userProfile.badges) { badge ->
                            BadgeCard(badge = badge)
                        }
                    }
                }
            }

            // My Registered Matches
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "🎮 My Registered Tournaments (${myRegistrations.size})",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Check your assigned slot numbers and room status.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            if (myRegistrations.isEmpty()) {
                item {
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You haven't joined any tournaments yet. Head over to the Arena to register!",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(myRegistrations) { reg ->
                    val tourney = tournaments.find { it.id == reg.tournamentId }
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        onClick = {
                            tourney?.let { onNavigateTo(AppScreen.TOURNAMENT_DETAILS, it.id) }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = tourney?.title ?: "Registered Match",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Allocated Slot: #${reg.slotNumber} • Method: ${reg.paymentMethod}",
                                    color = FireBluePrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (tourney?.isRoomReleased == true) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ToxicGreen.copy(alpha = 0.2f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(text = "Room Released 🔑", color = ToxicGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Text(text = "Locked 🔒", color = TextMuted, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Quick App Navigation Links
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Information & Support", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileNavigationRow(label = "About S_SHORIF eSports", icon = Icons.Default.Info) {
                        onNavigateTo(AppScreen.ABOUT, null)
                    }
                    ProfileNavigationRow(label = "24/7 Support & FAQs", icon = Icons.Default.Help) {
                        onNavigateTo(AppScreen.SUPPORT, null)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    highlightColor: Color = TextPrimary
) {
    GlassmorphicCard(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = highlightColor, fontSize = 15.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun BadgeCard(badge: BadgeItem) {
    GlassmorphicCard(
        modifier = Modifier.width(130.dp),
        shape = RoundedCornerShape(12.dp),
        borderColor = if (badge.isUnlocked) GoldRank.copy(alpha = 0.6f) else BorderSubtle
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = badge.iconEmoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = badge.title,
                color = if (badge.isUnlocked) TextPrimary else TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = badge.description,
                color = TextSecondary,
                fontSize = 9.sp,
                lineHeight = 12.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun ProfileNavigationRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = FireBluePrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = label, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}
