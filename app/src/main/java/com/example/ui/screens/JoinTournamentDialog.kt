package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Tournament
import com.example.ui.components.GlowingButton
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldRank
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ToxicGreen

@Composable
fun JoinTournamentDialog(
    tournament: Tournament,
    ign: String,
    onIgnChange: (String) -> Unit,
    uid: String,
    onUidChange: (String) -> Unit,
    team: String,
    onTeamChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    paymentMethod: String,
    onPaymentMethodChange: (String) -> Unit,
    trxId: String,
    onTrxIdChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            color = DeepNavyBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlow.copy(alpha = 0.6f)),
            shadowElevation = 24.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Register for Match",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = tournament.title,
                            color = FireBluePrimary,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("dialog_close_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Divider(color = BorderSubtle)

                if (successMessage != null) {
                    // Success View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ToxicGreen,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Slot Confirmed!",
                            color = ToxicGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = successMessage,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlowingButton(
                            text = "Done / View Details",
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "btn_dialog_done"
                        )
                    }
                } else {
                    // Slot Preview Badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CardNavyElevated)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Next Auto-Allocated Slot:",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Slot #${tournament.filledSlots + 1}",
                            color = FlameOrange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    // IGN Input
                    InputField(
                        label = "Free Fire In-Game Name (IGN) *",
                        value = ign,
                        onValueChange = onIgnChange,
                        placeholder = "e.g. SHORIF_BOSS",
                        testTag = "dialog_input_ign"
                    )

                    // UID Input
                    InputField(
                        label = "Free Fire Player UID *",
                        value = uid,
                        onValueChange = { onUidChange(it.filter { c -> c.isDigit() }) },
                        placeholder = "e.g. 5928194821",
                        testTag = "dialog_input_uid"
                    )

                    // Team Name
                    InputField(
                        label = "Team / Squad Name (Optional)",
                        value = team,
                        onValueChange = onTeamChange,
                        placeholder = "e.g. S_SHORIF_ELITE",
                        testTag = "dialog_input_team"
                    )

                    // Contact Phone
                    InputField(
                        label = "Contact Phone (SMS Alerts)",
                        value = phone,
                        onValueChange = onPhoneChange,
                        placeholder = "017XXXXXXXX",
                        testTag = "dialog_input_phone"
                    )

                    // Payment Gateway Section if Entry Fee > 0
                    if (tournament.entryFee > 0) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Select Payment Gateway (৳ ${tournament.entryFee})",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("bKash", "Nagad", "Rocket").forEach { method ->
                                    val isSelected = paymentMethod == method
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) FireBluePrimary else CardNavyElevated)
                                            .clickable { onPaymentMethodChange(method) }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = method,
                                            color = if (isSelected) DeepNavyBg else TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Payment instruction
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CardNavyElevated)
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Send ৳${tournament.entryFee} to $paymentMethod: 01799-887766 (Personal)",
                                        color = GoldRank,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Tap below to auto-fill a demo TrxID for rapid testing:",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "👉 Fill Demo TrxID: TRX${(100000..999999).random()}",
                                        color = SkyBlueAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .clickable {
                                                onTrxIdChange("TRX" + (7000000..9999999).random().toString())
                                            }
                                            .padding(vertical = 4.dp)
                                    )
                                }
                            }

                            // TrxID Input
                            InputField(
                                label = "Transaction ID (TrxID) *",
                                value = trxId,
                                onValueChange = onTrxIdChange,
                                placeholder = "e.g. TRX88492019",
                                testTag = "dialog_input_trx_id"
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = DangerRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Action Button
                    GlowingButton(
                        text = if (isLoading) "Verifying & Allocating..." else "Confirm Registration",
                        icon = Icons.Default.FlashOn,
                        onClick = onSubmit,
                        isLoading = isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "btn_submit_join_tournament"
                    )
                }
            }
        }
    }
}
