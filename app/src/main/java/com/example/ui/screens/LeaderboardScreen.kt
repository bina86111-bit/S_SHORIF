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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardPlayer
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BronzeRank
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GoldRank
import com.example.ui.theme.SilverRank
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LeaderboardScreen(
    players: List<LeaderboardPlayer>,
    selectedPeriod: String,
    onPeriodChange: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val topThree = players.take(3)
    val remaining = players.drop(3)

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
                        text = "🏆 Free Fire Hall of Fame",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Rankings based on total kills, Booyah victories, and reward points.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Time filter
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardNavyElevated)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("Today", "Weekly", "All Time").forEach { period ->
                            val isSelected = selectedPeriod == period
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) FireBluePrimary else Color.Transparent)
                                    .clickable { onPeriodChange(period) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = period,
                                    color = if (isSelected) DeepNavyBg else TextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Top 3 Podium
            if (topThree.isNotEmpty()) {
                item {
                    PodiumSection(topThree = topThree)
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Rank & Player", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Kills / Wins / Points", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Remaining players
            items(remaining) { player ->
                LeaderboardRowItem(player = player)
            }
        }
    }
}

@Composable
fun PodiumSection(topThree: List<LeaderboardPlayer>) {
    val first = topThree.getOrNull(0)
    val second = topThree.getOrNull(1)
    val third = topThree.getOrNull(2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd Place (Left)
        if (second != null) {
            PodiumCard(
                player = second,
                rankLabel = "2nd",
                rankColor = SilverRank,
                height = 160.dp,
                modifier = Modifier.weight(1f)
            )
        }

        // 1st Place (Center - Tallest)
        if (first != null) {
            PodiumCard(
                player = first,
                rankLabel = "1st 👑",
                rankColor = GoldRank,
                height = 190.dp,
                modifier = Modifier.weight(1.15f)
            )
        }

        // 3rd Place (Right)
        if (third != null) {
            PodiumCard(
                player = third,
                rankLabel = "3rd",
                rankColor = BronzeRank,
                height = 145.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PodiumCard(
    player: LeaderboardPlayer,
    rankLabel: String,
    rankColor: Color,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    GlassmorphicCard(
        modifier = modifier.height(height),
        borderColor = rankColor.copy(alpha = 0.6f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(rankColor.copy(alpha = 0.2f))
                    .border(0.5.dp, rankColor, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = rankLabel,
                    color = rankColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Avatar Emoji
            Text(text = player.avatarEmoji, fontSize = 28.sp)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = player.ign,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${player.points} pts",
                    color = FireBluePrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${player.kills} Kills • ${player.wins} Wins",
                    color = TextSecondary,
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun LeaderboardRowItem(player: LeaderboardPlayer) {
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
                // Rank number
                Text(
                    text = "#${player.rank}",
                    color = FireBluePrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.width(32.dp)
                )

                Text(text = player.avatarEmoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = player.ign,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = player.tier,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${player.points} pts",
                    color = GoldRank,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${player.kills} Kills | ${player.wins} Wins",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
    }
}
