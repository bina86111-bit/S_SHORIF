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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class WalletTx(
    val title: String,
    val amount: String,
    val isCredit: Boolean,
    val method: String,
    val date: String
)

@Composable
fun WalletScreen(
    walletBalance: Double,
    onDeposit: (Double, String, String) -> Unit,
    onWithdraw: (Double, String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var activeModalTab by remember { mutableStateOf<String?>(null) } // "DEPOSIT" or "WITHDRAW" or null
    var selectedGateway by remember { mutableStateOf("bKash") }
    var inputAmount by remember { mutableStateOf("100") }
    var inputNumber by remember { mutableStateOf("01712345678") }
    var inputTrxId by remember { mutableStateOf("") }

    val recentTxs = listOf(
        WalletTx("Prize Won: Bermuda Rush Hour 1st", "+ ৳ 700", true, "bKash", "Today, 02:30 PM"),
        WalletTx("Match Entry: S_SHORIF Championship #42", "- ৳ 100", false, "bKash", "Today, 06:15 PM"),
        WalletTx("Wallet Top-Up Deposit", "+ ৳ 500", true, "Nagad", "Yesterday, 09:12 PM"),
        WalletTx("Prize Won: Solo Scrims 3 Kills", "+ ৳ 60", true, "Wallet", "2 days ago"),
        WalletTx("Withdrawal to Personal Account", "- ৳ 300", false, "Rocket", "3 days ago")
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💳 S_SHORIF Wallet & Payouts",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Manage your tournament entry credits and instant prize withdrawals.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            // Wallet Balance Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(FireBluePrimary, FlameOrange)),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(CardNavyElevated, CardNavy)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "AVAILABLE EARNINGS & CREDITS",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "৳",
                                color = FlameOrange,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format("%.2f", walletBalance),
                                color = TextPrimary,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "BDT",
                                color = SkyBlueAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons: Deposit & Withdraw
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GlowingButton(
                                text = "Deposit Funds",
                                icon = Icons.Default.Add,
                                onClick = { activeModalTab = "DEPOSIT" },
                                modifier = Modifier.weight(1f),
                                testTag = "btn_wallet_deposit"
                            )

                            GlowingButton(
                                text = "Claim Prize",
                                icon = Icons.Default.EmojiEvents,
                                onClick = { activeModalTab = "WITHDRAW" },
                                isSecondary = true,
                                modifier = Modifier.weight(1f),
                                testTag = "btn_wallet_claim"
                            )
                        }
                    }
                }
            }

            // Interactive Deposit / Withdraw Section
            if (activeModalTab != null) {
                item {
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        borderColor = if (activeModalTab == "DEPOSIT") FireBluePrimary else FlameOrange
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (activeModalTab == "DEPOSIT") "💰 Add Funds to Wallet" else "🎁 Withdraw Prize to Mobile Bank",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Cancel",
                                    color = TextMuted,
                                    fontSize = 12.sp,
                                    modifier = Modifier.clickable { activeModalTab = null }
                                )
                            }

                            // Payment Gateways
                            Text(text = "Choose Gateway:", color = TextSecondary, fontSize = 12.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("bKash", "Nagad", "Rocket").forEach { gateway ->
                                    val isSelected = selectedGateway == gateway
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) FireBluePrimary else CardNavyElevated)
                                            .clickable { selectedGateway = gateway }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = gateway,
                                            color = if (isSelected) DeepNavyBg else TextPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            // Amount
                            InputField(
                                label = "Amount (৳ BDT)",
                                value = inputAmount,
                                onValueChange = { inputAmount = it.filter { c -> c.isDigit() } },
                                placeholder = "100",
                                testTag = "wallet_input_amount"
                            )

                            if (activeModalTab == "DEPOSIT") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CardNavyElevated)
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Send Money ৳$inputAmount to $selectedGateway Merchant: 01799-887766",
                                            color = GoldRank,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "👉 Quick Demo TrxID: TRX${(1000000..9999999).random()}",
                                            color = SkyBlueAccent,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable {
                                                    inputTrxId = "TRX" + (7000000..9999999).random().toString()
                                                }
                                                .padding(top = 4.dp)
                                        )
                                    }
                                }

                                InputField(
                                    label = "Transaction ID (TrxID)",
                                    value = inputTrxId,
                                    onValueChange = { inputTrxId = it },
                                    placeholder = "e.g. TRX9948201",
                                    testTag = "wallet_input_trx_id"
                                )

                                GlowingButton(
                                    text = "Confirm ৳$inputAmount Deposit",
                                    onClick = {
                                        val amt = inputAmount.toDoubleOrNull() ?: 50.0
                                        val trx = if (inputTrxId.isNotBlank()) inputTrxId else "DEMO-TRX-101"
                                        onDeposit(amt, selectedGateway, trx)
                                        activeModalTab = null
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    testTag = "btn_submit_deposit"
                                )
                            } else {
                                InputField(
                                    label = "$selectedGateway Number for Payout",
                                    value = inputNumber,
                                    onValueChange = { inputNumber = it },
                                    placeholder = "017XXXXXXXX",
                                    testTag = "wallet_input_phone"
                                )

                                GlowingButton(
                                    text = "Submit ৳$inputAmount Payout Request",
                                    onClick = {
                                        val amt = inputAmount.toDoubleOrNull() ?: 100.0
                                        if (walletBalance < amt) {
                                            Toast.makeText(context, "Insufficient balance!", Toast.LENGTH_SHORT).show()
                                            return@GlowingButton
                                        }
                                        onWithdraw(amt, selectedGateway, inputNumber)
                                        activeModalTab = null
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    testTag = "btn_submit_withdraw"
                                )
                            }
                        }
                    }
                }
            }

            // Supported Gateways Info Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GatewayBadge(name = "bKash Verified", icon = "🟣", modifier = Modifier.weight(1f))
                    GatewayBadge(name = "Nagad Direct", icon = "🟠", modifier = Modifier.weight(1f))
                    GatewayBadge(name = "Rocket Instant", icon = "🟣", modifier = Modifier.weight(1f))
                }
            }

            // Recent Transactions Section
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📜 Payment & Reward History",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Verified transactions & prize pool distribution records.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            items(recentTxs) { tx ->
                TransactionCard(tx = tx)
            }
        }
    }
}

@Composable
fun GatewayBadge(name: String, icon: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CardNavyElevated)
            .border(0.5.dp, BorderSubtle, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun TransactionCard(tx: WalletTx) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (tx.isCredit) ToxicGreen.copy(alpha = 0.2f) else DangerRed.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (tx.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = if (tx.isCredit) ToxicGreen else DangerRed,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = tx.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${tx.method} • ${tx.date}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Text(
                text = tx.amount,
                color = if (tx.isCredit) ToxicGreen else TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
