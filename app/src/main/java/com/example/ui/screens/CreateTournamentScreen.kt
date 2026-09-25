package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MapType
import com.example.data.model.MatchMode
import com.example.ui.components.GlassmorphicCard
import com.example.ui.components.GlowingButton
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreateTournamentScreen(
    onBack: () -> Unit,
    onCreateTournament: (
        title: String,
        matchMode: MatchMode,
        mapType: MapType,
        scheduleTime: String,
        entryFee: Int,
        prizePool: Int,
        firstPrize: Int,
        perKillBonus: Int,
        totalSlots: Int,
        rules: List<String>
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var title by remember { mutableStateOf("S_SHORIF Clash Squad Night") }
    var selectedMode by remember { mutableStateOf(MatchMode.SQUAD) }
    var selectedMap by remember { mutableStateOf(MapType.BERMUDA) }
    var scheduleTime by remember { mutableStateOf("Tonight, 09:00 PM") }
    var entryFee by remember { mutableStateOf("50") }
    var prizePool by remember { mutableStateOf("2000") }
    var firstPrize by remember { mutableStateOf("1200") }
    var perKillBonus by remember { mutableStateOf("20") }
    var totalSlots by remember { mutableStateOf("48") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("create_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = FireBluePrimary
                        )
                    }
                    Text(
                        text = "Host Free Fire Tournament",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Form Card
            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    borderColor = FireBluePrimary.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Title
                        InputField(
                            label = "Tournament Title",
                            value = title,
                            onValueChange = { title = it },
                            placeholder = "e.g. S_SHORIF Bermuda Championship",
                            testTag = "input_tourney_title"
                        )

                        // Mode Selector
                        Text("Match Mode", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MatchMode.values().forEach { mode ->
                                FilterModeChip(
                                    label = mode.displayName,
                                    isSelected = selectedMode == mode,
                                    onClick = { selectedMode = mode },
                                    testTag = "select_mode_${mode.name.lowercase()}"
                                )
                            }
                        }

                        // Map Selector
                        Text("Map", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            MapType.values().forEach { map ->
                                FilterModeChip(
                                    label = map.displayName,
                                    isSelected = selectedMap == map,
                                    onClick = { selectedMap = map },
                                    testTag = "select_map_${map.name.lowercase()}"
                                )
                            }
                        }

                        // Schedule Time
                        InputField(
                            label = "Date & Schedule Time",
                            value = scheduleTime,
                            onValueChange = { scheduleTime = it },
                            placeholder = "e.g. Today, 08:30 PM",
                            testTag = "input_tourney_schedule"
                        )

                        // Financials Row: Entry Fee & Prize Pool
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            InputField(
                                label = "Entry Fee (৳ BDT)",
                                value = entryFee,
                                onValueChange = { entryFee = it.filter { char -> char.isDigit() } },
                                placeholder = "0 for Free",
                                modifier = Modifier.weight(1f),
                                testTag = "input_tourney_entry_fee"
                            )

                            InputField(
                                label = "Total Prize Pool (৳)",
                                value = prizePool,
                                onValueChange = { prizePool = it.filter { char -> char.isDigit() } },
                                placeholder = "2000",
                                modifier = Modifier.weight(1f),
                                testTag = "input_tourney_prize_pool"
                            )
                        }

                        // Financials Row: 1st Prize & Per Kill
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            InputField(
                                label = "1st Place Prize (৳)",
                                value = firstPrize,
                                onValueChange = { firstPrize = it.filter { char -> char.isDigit() } },
                                placeholder = "1200",
                                modifier = Modifier.weight(1f),
                                testTag = "input_tourney_first_prize"
                            )

                            InputField(
                                label = "Per Kill Bonus (৳)",
                                value = perKillBonus,
                                onValueChange = { perKillBonus = it.filter { char -> char.isDigit() } },
                                placeholder = "20",
                                modifier = Modifier.weight(1f),
                                testTag = "input_tourney_kill_bonus"
                            )
                        }

                        // Slot Limit
                        Text("Slot Limit (Max Players / Squads)", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("12", "24", "48").forEach { slots ->
                                FilterModeChip(
                                    label = "$slots Slots",
                                    isSelected = totalSlots == slots,
                                    onClick = { totalSlots = slots },
                                    testTag = "select_slots_$slots"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Submit
                        GlowingButton(
                            text = "Publish Tournament & Auto-Generate Room",
                            icon = Icons.Default.FlashOn,
                            onClick = {
                                if (title.isBlank()) {
                                    Toast.makeText(context, "Please enter a tournament title", Toast.LENGTH_SHORT).show()
                                    return@GlowingButton
                                }
                                val fee = entryFee.toIntOrNull() ?: 0
                                val prize = prizePool.toIntOrNull() ?: 1000
                                val first = firstPrize.toIntOrNull() ?: (prize * 0.6).toInt()
                                val kill = perKillBonus.toIntOrNull() ?: 15
                                val slots = totalSlots.toIntOrNull() ?: 48

                                onCreateTournament(
                                    title.trim(),
                                    selectedMode,
                                    selectedMap,
                                    scheduleTime.trim(),
                                    fee,
                                    prize,
                                    first,
                                    kill,
                                    slots,
                                    listOf(
                                        "No Emulator, Hack, or Config files allowed.",
                                        "Room ID and Pass published 15 minutes before match.",
                                        "Gun properties OFF, Character skills standard.",
                                        "Victory screenshot mandatory for instant bKash prize claim."
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "btn_submit_create_tournament"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Column(modifier = modifier) {
        Text(text = label, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 13.sp) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FireBluePrimary,
                unfocusedBorderColor = BorderSubtle,
                focusedContainerColor = CardNavyElevated,
                unfocusedContainerColor = CardNavyElevated,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )
    }
}
