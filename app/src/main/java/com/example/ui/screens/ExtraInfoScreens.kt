package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
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
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldRank
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("about_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = FireBluePrimary)
                    }
                    Text(
                        text = "About S_SHORIF eSports",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    borderColor = FireBluePrimary.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(listOf(FireBlueDark, FireBluePrimary))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "⚡", fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "S_SHORIF eSports", color = FireBluePrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                Text(text = "Premier Free Fire Tournament Platform", color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Divider(color = BorderSubtle)

                        Text(
                            text = "S_SHORIF is a high-performance tournament management and organization hub engineered specifically for the competitive Free Fire community.\n\nFounded to bring professional eSports standards to grass-roots scrims and major championships, S_SHORIF features automated slot distribution, instant room credential broadcasting, guaranteed bKash/Nagad prize payouts, and neural AI-driven anti-cheat detection.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            // Contact Info
            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "📞 Official Contact & Community", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                        ContactRow(icon = Icons.Default.Email, label = "Email", value = "support@sshorif-esports.com")
                        ContactRow(icon = Icons.Default.Phone, label = "Helpline", value = "+880 1799-887766")
                        ContactRow(icon = Icons.Default.Language, label = "Discord / Telegram", value = "t.me/sshorif_freefire")
                    }
                }
            }

            // Terms & Fair Play Summary
            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "⚖️ Fair Play & Terms Summary", color = FlameOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "1. Gun properties are turned OFF in all competitive rooms.\n2. Teaming, emulator bypass, or macro scripts result in permanent device ban.\n3. Prize claims must include unedited match victory screenshots.\n4. Refunds provided in full if tournament canceled by organizers.",
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
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = SkyBlueAccent, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, color = TextMuted, fontSize = 10.sp)
            Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

data class FaqItem(val question: String, val answer: String)

@Composable
fun SupportScreen(
    onNavigateToAiChat: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val faqs = listOf(
        FaqItem(
            "How and when do I get the Room ID and Password?",
            "The Room ID and Password appear directly in the tournament details screen exactly 15 minutes before the match start time. You will also receive an in-app notification alert."
        ),
        FaqItem(
            "How do I receive my prize money after winning?",
            "Prize money is credited to your S_SHORIF wallet within 30 minutes of submitting your Booyah victory screenshot. You can withdraw anytime to your bKash, Nagad, or Rocket account."
        ),
        FaqItem(
            "What if a player uses cheats or emulator in custom room?",
            "Our S_SHORIF AI Anti-Cheat logs all player movements and stat anomalies. Report the player UID to our support team or AI assistant for instant inspection and disqualification."
        ),
        FaqItem(
            "Can I host my own tournament on S_SHORIF?",
            "Yes! Tap 'Host Match' on the home screen, set your prize pool, entry fee, map, and slot count. Room credentials and player allocations are automatically generated."
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("support_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = FireBluePrimary)
                    }
                    Text(
                        text = "24/7 Help & Support",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // AI Instant Chat Help Banner
            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    borderColor = FireBluePrimary.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = FireBluePrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Chat with S_SHORIF AI Bot", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Get immediate answers about match rules, room keys, and payment confirmation from our automated AI agent.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        GlowingButton(
                            text = "Open AI Chat Assistant",
                            icon = Icons.Default.AutoAwesome,
                            onClick = onNavigateToAiChat,
                            modifier = Modifier.fillMaxWidth(),
                            testTag = "btn_open_ai_chat_support"
                        )
                    }
                }
            }

            // FAQs Header
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(text = "❓ Frequently Asked Questions", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Everything you need to know about S_SHORIF tournaments.", color = TextSecondary, fontSize = 12.sp)
                }
            }

            items(faqs) { faq ->
                FaqAccordion(faq = faq)
            }
        }
    }
}

@Composable
fun FaqAccordion(faq: FaqItem) {
    var isExpanded by remember { mutableStateOf(false) }

    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faq.question,
                    color = if (isExpanded) FireBluePrimary else TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = FireBluePrimary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = faq.answer,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}
