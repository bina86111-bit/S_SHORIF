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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Tournament
import com.example.data.repository.AuthenticityReport
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlowingButton
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
import com.example.ui.viewmodel.ChatMessage

@Composable
fun AiHubScreen(
    chatMessages: List<ChatMessage>,
    chatInput: String,
    onChatInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isAiThinking: Boolean,
    tournaments: List<Tournament>,
    highlightsOutput: String?,
    isGeneratingHighlights: Boolean,
    onGenerateHighlights: (String) -> Unit,
    antiCheatIgn: String,
    onAntiCheatIgnChange: (String) -> Unit,
    antiCheatUid: String,
    onAntiCheatUidChange: (String) -> Unit,
    antiCheatLevel: String,
    onAntiCheatLevelChange: (String) -> Unit,
    antiCheatKd: String,
    onAntiCheatKdChange: (String) -> Unit,
    antiCheatReport: AuthenticityReport?,
    isCheckingAntiCheat: Boolean,
    onRunAntiCheatCheck: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var selectedAiTab by remember { mutableStateOf(0) }
    val aiTabs = listOf("S_SHORIF AI Assistant", "Anti-Cheat Detector", "Highlights Caster")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = FireBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "S_SHORIF AI Hub",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Text(
                    text = "eSports AI intelligence, automated match caster, and anti-cheat validation.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            // Tab Selector
            ScrollableTabRow(
                selectedTabIndex = selectedAiTab,
                containerColor = DeepNavyBg,
                contentColor = FireBluePrimary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedAiTab]),
                        color = FireBluePrimary,
                        height = 3.dp
                    )
                },
                divider = { Divider(color = BorderSubtle) }
            ) {
                aiTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedAiTab == index,
                        onClick = { selectedAiTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedAiTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedAiTab == index) FireBluePrimary else TextSecondary
                            )
                        },
                        modifier = Modifier.testTag("ai_tab_$index")
                    )
                }
            }

            // Tab Content
            when (selectedAiTab) {
                0 -> {
                    // Chat Assistant
                    AiChatAssistantView(
                        chatMessages = chatMessages,
                        chatInput = chatInput,
                        onChatInputChange = onChatInputChange,
                        onSendMessage = onSendMessage,
                        isAiThinking = isAiThinking
                    )
                }
                1 -> {
                    // Anti Cheat Detector
                    AntiCheatDetectorView(
                        ign = antiCheatIgn,
                        onIgnChange = onAntiCheatIgnChange,
                        uid = antiCheatUid,
                        onUidChange = onAntiCheatUidChange,
                        level = antiCheatLevel,
                        onLevelChange = onAntiCheatLevelChange,
                        kd = antiCheatKd,
                        onKdChange = onAntiCheatKdChange,
                        report = antiCheatReport,
                        isChecking = isCheckingAntiCheat,
                        onCheck = onRunAntiCheatCheck
                    )
                }
                2 -> {
                    // Event Highlights Caster
                    AiHighlightsView(
                        tournaments = tournaments,
                        highlightsOutput = highlightsOutput,
                        isGenerating = isGeneratingHighlights,
                        onGenerate = onGenerateHighlights
                    )
                }
            }
        }
    }
}

@Composable
fun AiChatAssistantView(
    chatMessages: List<ChatMessage>,
    chatInput: String,
    onChatInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    isAiThinking: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
    ) {
        // Quick prompts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val suggestions = listOf("How to get Room ID?", "bKash Payouts?", "Current Meta?")
            suggestions.forEach { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardNavyElevated)
                        .border(0.5.dp, BorderSubtle, RoundedCornerShape(20.dp))
                        .clickable {
                            onChatInputChange(prompt)
                            onSendMessage()
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = prompt, color = SkyBlueAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            reverseLayout = false
        ) {
            items(chatMessages) { msg ->
                ChatBubble(msg = msg)
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (isAiThinking) {
                item {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = FireBluePrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "S_SHORIF AI is analyzing...", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }

        // Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardNavy)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = onChatInputChange,
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_chat_input"),
                placeholder = { Text("Ask S_SHORIF AI anything...", color = TextMuted, fontSize = 13.sp) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FireBluePrimary,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = CardNavyElevated,
                    unfocusedContainerColor = CardNavyElevated,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSendMessage,
                modifier = Modifier
                    .testTag("btn_send_ai_chat")
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(FireBluePrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = DeepNavyBg,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!msg.isFromUser) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(FireBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚡", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (msg.isFromUser) 16.dp else 4.dp,
                        bottomEnd = if (msg.isFromUser) 4.dp else 16.dp
                    )
                )
                .background(if (msg.isFromUser) FireBlueDark else CardNavyElevated)
                .border(
                    0.5.dp,
                    if (msg.isFromUser) BorderGlow else BorderSubtle,
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                text = msg.text,
                color = if (msg.isFromUser) Color.White else TextPrimary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun AntiCheatDetectorView(
    ign: String,
    onIgnChange: (String) -> Unit,
    uid: String,
    onUidChange: (String) -> Unit,
    level: String,
    onLevelChange: (String) -> Unit,
    kd: String,
    onKdChange: (String) -> Unit,
    report: AuthenticityReport?,
    isChecking: Boolean,
    onCheck: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = FireBluePrimary.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = FireBluePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Fake Player & Anti-Cheat Scan",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Scans player Garena UID, level correlation, KD spikes, and device telemetry to detect emulators, macro scripts, or fake identities.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    InputField(
                        label = "Player In-Game Name (IGN)",
                        value = ign,
                        onValueChange = onIgnChange,
                        placeholder = "e.g. SHORIF_BOSS",
                        testTag = "anticheat_input_ign"
                    )

                    InputField(
                        label = "Player Free Fire UID",
                        value = uid,
                        onValueChange = onUidChange,
                        placeholder = "e.g. 5928194821",
                        testTag = "anticheat_input_uid"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        InputField(
                            label = "Reported Level",
                            value = level,
                            onValueChange = onLevelChange,
                            placeholder = "65",
                            modifier = Modifier.weight(1f),
                            testTag = "anticheat_input_level"
                        )

                        InputField(
                            label = "Reported K/D",
                            value = kd,
                            onValueChange = onKdChange,
                            placeholder = "5.6",
                            modifier = Modifier.weight(1f),
                            testTag = "anticheat_input_kd"
                        )
                    }

                    GlowingButton(
                        text = if (isChecking) "Running Neural Analysis..." else "Verify Player Authenticity",
                        icon = Icons.Default.FactCheck,
                        onClick = onCheck,
                        isLoading = isChecking,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "btn_run_anticheat"
                    )
                }
            }
        }

        if (report != null) {
            item {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (report.isApproved) ToxicGreen else DangerRed
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "VERIFICATION REPORT",
                                color = if (report.isApproved) ToxicGreen else DangerRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )

                            Text(
                                text = "Risk Score: ${report.riskScore}%",
                                color = if (report.riskScore < 30) ToxicGreen else if (report.riskScore < 70) FlameOrange else DangerRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = report.verdict,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Divider(color = BorderSubtle)

                        report.checks.forEach { check ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "• ", color = FireBluePrimary, fontWeight = FontWeight.Bold)
                                Text(text = check, color = TextSecondary, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AiHighlightsView(
    tournaments: List<Tournament>,
    highlightsOutput: String?,
    isGenerating: Boolean,
    onGenerate: (String) -> Unit
) {
    var selectedTournament by remember { mutableStateOf(tournaments.firstOrNull()?.title ?: "S_SHORIF Grand Championship #42") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = GoldRank.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldRank)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Match Caster & Highlights",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Select any tournament event to auto-generate a dramatic eSports broadcast summary with match MVPs and zone breakdowns.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    // Selectable Tournaments
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        tournaments.forEach { t ->
                            val isSelected = selectedTournament == t.title
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) FireBluePrimary.copy(alpha = 0.2f) else CardNavyElevated)
                                    .border(0.5.dp, if (isSelected) FireBluePrimary else BorderSubtle, RoundedCornerShape(8.dp))
                                    .clickable { selectedTournament = t.title }
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "🏆 ${t.title} (${t.mapType.displayName})",
                                    color = if (isSelected) FireBluePrimary else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    GlowingButton(
                        text = if (isGenerating) "AI Generating Highlights..." else "Generate Event Highlights",
                        icon = Icons.Default.AutoAwesome,
                        onClick = { onGenerate(selectedTournament) },
                        isLoading = isGenerating,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "btn_generate_highlights_hub"
                    )
                }
            }
        }

        if (highlightsOutput != null) {
            item {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = GoldRank
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🎙️ AI Broadcast Summary",
                            color = GoldRank,
                            fontSize = 15.sp,
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
}
