package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SlotRegistration
import com.example.data.model.Tournament
import com.example.data.model.TournamentStatus
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlowingButton
import com.example.ui.components.MapBadge
import com.example.ui.components.ModeBadge
import com.example.ui.components.SlotProgressBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DangerRed
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

@Composable
fun TournamentDetailScreen(
    tournament: Tournament?,
    registrations: List<SlotRegistration>,
    onBack: () -> Unit,
    onJoinClick: (Tournament) -> Unit,
    onToggleRoomRelease: (String) -> Unit,
    onGenerateHighlights: (String) -> Unit,
    highlightsOutput: String?,
    isGeneratingHighlights: Boolean,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Slots (${registrations.size}/${tournament?.totalSlots ?: 48})", "Rules & Matrix", "AI Caster")

    if (tournament == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(DeepNavyBg),
            contentAlignment = Alignment.Center
        ) {
            Text("Tournament not found", color = TextSecondary)
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = FireBluePrimary
                        )
                    }

                    Text(
                        text = "Tournament Details",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Tournament link copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = SkyBlueAccent
                        )
                    }
                }
            }

            // Tournament Banner Card
            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    borderColor = FireBluePrimary.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ModeBadge(mode = tournament.matchMode)
                                MapBadge(mapType = tournament.mapType)
                            }
                            StatusBadge(status = tournament.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = tournament.title,
                            color = TextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "🗓️ Schedule: ${tournament.scheduleTime}",
                            color = SkyBlueAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SlotProgressBar(filled = tournament.filledSlots, total = tournament.totalSlots)
                    }
                }
            }

            // Room Release Info Board
            item {
                RoomCredentialsBoard(
                    tournament = tournament,
                    onToggleRelease = { onToggleRoomRelease(tournament.id) },
                    onCopy = { label, text ->
                        clipboardManager.setText(AnnotatedString(text))
                        Toast.makeText(context, "$label copied: $text", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Tabs Header
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DeepNavyBg,
                    contentColor = FireBluePrimary,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = FireBluePrimary,
                            height = 3.dp
                        )
                    },
                    divider = { Divider(color = BorderSubtle) }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == index) FireBluePrimary else TextSecondary
                                )
                            },
                            modifier = Modifier.testTag("detail_tab_$index")
                        )
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> {
                    // Overview
                    item {
                        TournamentOverviewTab(tournament = tournament)
                    }
                }
                1 -> {
                    // Registered Slots
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Allocated Slots (${registrations.size} Registered)",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Slots are automatically assigned in order of verified entry.",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                    if (registrations.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No players registered yet. Be the first to claim Slot #1!",
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    } else {
                        items(registrations) { reg ->
                            SlotRegistrationItem(reg = reg)
                        }
                    }
                }
                2 -> {
                    // Rules & Prize Matrix
                    item {
                        TournamentRulesTab(tournament = tournament)
                    }
                }
                3 -> {
                    // AI Caster & Highlights
                    item {
                        AiCasterTab(
                            tournament = tournament,
                            highlightsOutput = highlightsOutput,
                            isGenerating = isGeneratingHighlights,
                            onGenerate = { onGenerateHighlights(tournament.title) }
                        )
                    }
                }
            }
        }

        // Bottom Sticky Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .border(BorderStroke(0.5.dp, BorderSubtle)),
            color = DeepNavyBg.copy(alpha = 0.95f),
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Entry Fee",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (tournament.entryFee == 0) "FREE" else "৳ ${tournament.entryFee} BDT",
                        color = if (tournament.entryFee == 0) ToxicGreen else GoldRank,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                GlowingButton(
                    text = if (tournament.filledSlots >= tournament.totalSlots) "Slots Full" else "Register / Join Now",
                    icon = Icons.Default.FlashOn,
                    onClick = { onJoinClick(tournament) },
                    enabled = tournament.filledSlots < tournament.totalSlots,
                    testTag = "sticky_join_tournament_button"
                )
            }
        }
    }
}

@Composable
fun RoomCredentialsBoard(
    tournament: Tournament,
    onToggleRelease: () -> Unit,
    onCopy: (String, String) -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        borderColor = if (tournament.isRoomReleased) ToxicGreen else BorderGlow,
        testTag = "room_credentials_board"
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (tournament.isRoomReleased) Icons.Default.Key else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (tournament.isRoomReleased) ToxicGreen else FlameOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (tournament.isRoomReleased) "ROOM CREDENTIALS LIVE" else "ROOM CREDENTIALS BOARD",
                        color = if (tournament.isRoomReleased) ToxicGreen else FlameOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Admin simulation toggle
                Text(
                    text = if (tournament.isRoomReleased) "🔒 Hide" else "⚡ Host Release",
                    color = FireBluePrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CardNavyElevated)
                        .border(0.5.dp, FireBluePrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .clickable(onClick = onToggleRelease)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (tournament.isRoomReleased) {
                // Room Released Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardNavyElevated)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Custom Room ID", color = TextSecondary, fontSize = 11.sp)
                        Text(
                            text = tournament.roomId,
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    IconButton(onClick = { onCopy("Room ID", tournament.roomId) }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Room ID", tint = FireBluePrimary)
                    }

                    Divider(
                        modifier = Modifier
                            .height(28.dp)
                            .width(1.dp),
                        color = BorderSubtle
                    )

                    Column {
                        Text(text = "Password", color = TextSecondary, fontSize = 11.sp)
                        Text(
                            text = tournament.roomPassword,
                            color = FlameOrange,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    IconButton(onClick = { onCopy("Password", tournament.roomPassword) }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Password", tint = FlameOrange)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✅ Open Free Fire > Custom > Join Room, paste credentials above.",
                    color = ToxicGreen,
                    fontSize = 11.sp
                )
            } else {
                // Locked Notice
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardNavyElevated.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🔒 Room ID & Password will be unlocked 15 minutes before ${tournament.scheduleTime}. Registered players will also get an instant notification alert.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TournamentOverviewTab(tournament: Tournament) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Prize Pool Breakdown Card
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🏆 Prize Pool Distribution",
                    color = GoldRank,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PrizeItem(rank = "🥇 1st Place (Booyah)", amount = "৳ ${tournament.firstPrize}", highlight = GoldRank)
                    PrizeItem(rank = "🎯 Per Kill", amount = "৳ ${tournament.perKillBonus}", highlight = FlameOrange)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PrizeItem(rank = "🥈 2nd Place", amount = "৳ ${tournament.prizePool / 4}", highlight = SkyBlueAccent)
                    PrizeItem(rank = "🥉 3rd Place", amount = "৳ ${tournament.prizePool / 8}", highlight = TextSecondary)
                }
            }
        }

        // Match Specifications
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚙️ Match Specifications",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                SpecRow(label = "Match Mode", value = "${tournament.matchMode.displayName} (${tournament.matchMode.teamSize} Players)")
                SpecRow(label = "Map", value = tournament.mapType.displayName)
                SpecRow(label = "Gun Attributes", value = "Disabled (Competitive)")
                SpecRow(label = "Character Skill", value = "Allowed (Standard)")
                SpecRow(label = "Revival System", value = "Disabled in Final Zones")
                SpecRow(label = "Organizer", value = tournament.organizer)
            }
        }
    }
}

@Composable
fun PrizeItem(rank: String, amount: String, highlight: Color) {
    Column {
        Text(text = rank, color = TextSecondary, fontSize = 11.sp)
        Text(text = amount, color = highlight, fontSize = 15.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp)
        Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SlotRegistrationItem(reg: SlotRegistration) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Slot Number Badge
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(CardNavyElevated)
                        .border(1.dp, FireBluePrimary.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#${reg.slotNumber}",
                        color = FireBluePrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = reg.ign,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "UID: ${reg.freeFireUid}" + (if (!reg.teamName.isNullOrBlank()) " • Team: ${reg.teamName}" else ""),
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ToxicGreen.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "CONFIRMED",
                    color = ToxicGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TournamentRulesTab(tournament: Tournament) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📜 S_SHORIF Official Rules & Guidelines",
                    color = FlameOrange,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                tournament.rules.forEachIndexed { idx, rule ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${idx + 1}. ",
                            color = FireBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = rule,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AiCasterTab(
    tournament: Tournament,
    highlightsOutput: String?,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = FireBluePrimary.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = FireBluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Match Caster & Commentary",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Generate real-time literary eSports analysis, MVP predictions, and zone movement breakdowns powered by S_SHORIF AI.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                GlowingButton(
                    text = if (isGenerating) "AI Analyzing Match..." else "Generate Match Analysis",
                    icon = Icons.Default.AutoAwesome,
                    onClick = onGenerate,
                    isLoading = isGenerating,
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "btn_generate_match_highlights"
                )
            }
        }

        if (highlightsOutput != null) {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GoldRank.copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎙️ AI Caster Broadcast Report",
                        color = GoldRank,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = highlightsOutput,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
