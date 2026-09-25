package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MatchMode
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
import com.example.ui.viewmodel.AppScreen

@Composable
fun HomeScreen(
    tournaments: List<Tournament>,
    selectedMode: MatchMode?,
    onSelectMode: (MatchMode?) -> Unit,
    onNavigateTo: (AppScreen, String?) -> Unit,
    onJoinTournament: (Tournament) -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredTournament = tournaments.find { it.isFeatured } ?: tournaments.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Esports Dashboard Banner
        item {
            HeroDashboardBanner(
                featured = featuredTournament,
                onJoinClick = { featuredTournament?.let { onJoinTournament(it) } },
                onCreateClick = { onNavigateTo(AppScreen.CREATE_TOURNAMENT, null) },
                onDetailsClick = { featuredTournament?.let { onNavigateTo(AppScreen.TOURNAMENT_DETAILS, it.id) } }
            )
        }

        // Live eSports Ticker / Quick Stats
        item {
            EsportsStatsTicker(
                activeCount = tournaments.count { it.status == TournamentStatus.REGISTRATION_OPEN || it.status == TournamentStatus.LIVE },
                totalPrizePool = tournaments.sumOf { it.prizePool },
                onAiHubClick = { onNavigateTo(AppScreen.AI_HUB, null) }
            )
        }

        // Mode Filter Chips
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "🎮 Active Tournaments",
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "View All (${tournaments.size})",
                        color = FireBluePrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .testTag("view_all_tournaments_link")
                            .clickable { onNavigateTo(AppScreen.TOURNAMENTS, null) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterModeChip(
                            label = "All Modes",
                            isSelected = selectedMode == null,
                            onClick = { onSelectMode(null) },
                            testTag = "filter_mode_all"
                        )
                    }
                    items(MatchMode.values()) { mode ->
                        FilterModeChip(
                            label = mode.displayName,
                            isSelected = selectedMode == mode,
                            onClick = { onSelectMode(mode) },
                            testTag = "filter_mode_${mode.name.lowercase()}"
                        )
                    }
                }
            }
        }

        // Tournaments List
        items(tournaments) { tournament ->
            TournamentCard(
                tournament = tournament,
                onCardClick = { onNavigateTo(AppScreen.TOURNAMENT_DETAILS, tournament.id) },
                onJoinClick = { onJoinTournament(tournament) }
            )
        }

        // S_SHORIF AI Assistant Card Callout
        item {
            AiBannerCallout(
                onExploreAi = { onNavigateTo(AppScreen.AI_HUB, null) }
            )
        }

        // Platform Trust Features Footer Card
        item {
            PlatformTrustCard(
                onRulesClick = { onNavigateTo(AppScreen.ABOUT, null) },
                onSupportClick = { onNavigateTo(AppScreen.SUPPORT, null) }
            )
        }
    }
}

@Composable
fun HeroDashboardBanner(
    featured: Tournament?,
    onJoinClick: () -> Unit,
    onCreateClick: () -> Unit,
    onDetailsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(
                1.dp,
                Brush.linearGradient(listOf(FireBluePrimary.copy(alpha = 0.8f), SkyBlueAccent.copy(alpha = 0.2f))),
                RoundedCornerShape(20.dp)
            )
    ) {
        // Hero Background Image with Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardNavy)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_esports),
                contentDescription = "S_SHORIF Arena",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )

            // Dynamic Dark Gradient Overlays for readable text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                DeepNavyBg.copy(alpha = 0.2f),
                                DeepNavyBg.copy(alpha = 0.75f),
                                DeepNavyBg.copy(alpha = 0.98f)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Top Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(FireBlueDark.copy(alpha = 0.85f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🔥 MAJOR CHAMPIONSHIP",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    if (featured != null) {
                        StatusBadge(status = featured.status)
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // Title & Subtitle
                Text(
                    text = featured?.title ?: "S_SHORIF Grand Championship #42",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🏆 Total Prize:",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "৳ ${featured?.prizePool ?: 3500}",
                        color = GoldRank,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "•  Entry: ${if ((featured?.entryFee ?: 0) == 0) "FREE" else "৳ ${featured?.entryFee}"}",
                        color = SkyBlueAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Slot Filling Progress in Hero
                if (featured != null) {
                    SlotProgressBar(
                        filled = featured.filledSlots,
                        total = featured.totalSlots
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlowingButton(
                        text = "Join Now",
                        icon = Icons.Default.FlashOn,
                        onClick = onJoinClick,
                        modifier = Modifier.weight(1f),
                        testTag = "hero_join_now_button"
                    )

                    GlowingButton(
                        text = "Host Match",
                        icon = Icons.Default.AddCircle,
                        onClick = onCreateClick,
                        isSecondary = true,
                        modifier = Modifier.weight(1f),
                        testTag = "hero_create_tournament_button"
                    )
                }
            }
        }
    }
}

@Composable
fun EsportsStatsTicker(
    activeCount: Int,
    totalPrizePool: Int,
    onAiHubClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Stat 1: Active
        GlassmorphicCard(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            testTag = "stat_active_tournaments"
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "ACTIVE",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$activeCount Matches",
                    color = FireBluePrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Stat 2: Total Pool
        GlassmorphicCard(
            modifier = Modifier.weight(1.2f),
            shape = RoundedCornerShape(12.dp),
            testTag = "stat_total_prize"
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "TOTAL PRIZE POOL",
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "৳ $totalPrizePool BDT",
                    color = GoldRank,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Stat 3: AI Assistant Callout
        GlassmorphicCard(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            borderColor = FireBluePrimary.copy(alpha = 0.5f),
            onClick = onAiHubClick,
            testTag = "stat_ai_bot"
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "S_SHORIF AI",
                    color = FlameOrange,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Online ⚡",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FilterModeChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bgColor = if (isSelected) FireBluePrimary else CardNavyElevated
    val textColor = if (isSelected) DeepNavyBg else TextSecondary
    val borderColor = if (isSelected) FireBluePrimary else BorderSubtle

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun TournamentCard(
    tournament: Tournament,
    onCardClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 7.dp),
        onClick = onCardClick,
        testTag = "tournament_card_${tournament.id}"
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Badges Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ModeBadge(mode = tournament.matchMode)
                    MapBadge(mapType = tournament.mapType)
                }
                StatusBadge(status = tournament.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = tournament.title,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "⏰ ${tournament.scheduleTime}",
                color = SkyBlueAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Metrics: Prize, Per Kill, Entry
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardNavyElevated.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Prize Pool", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "৳ ${tournament.prizePool}", color = GoldRank, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "1st Place", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "৳ ${tournament.firstPrize}", color = FireBluePrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "Per Kill", color = TextSecondary, fontSize = 11.sp)
                    Text(text = "৳ ${tournament.perKillBonus}", color = FlameOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "Entry", color = TextSecondary, fontSize = 11.sp)
                    Text(
                        text = if (tournament.entryFee == 0) "FREE" else "৳ ${tournament.entryFee}",
                        color = if (tournament.entryFee == 0) ToxicGreen else TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Slot Progress
            SlotProgressBar(filled = tournament.filledSlots, total = tournament.totalSlots)

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom action row: Room Info alert & Join Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Room info indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onCardClick)
                ) {
                    Icon(
                        imageVector = if (tournament.isRoomReleased) Icons.Default.Key else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (tournament.isRoomReleased) FireBluePrimary else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (tournament.isRoomReleased) "Room Ready 🔑" else "Room 15m before",
                        color = if (tournament.isRoomReleased) FireBluePrimary else TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                GlowingButton(
                    text = if (tournament.filledSlots >= tournament.totalSlots) "Full" else "Join ৳${tournament.entryFee}",
                    onClick = onJoinClick,
                    enabled = tournament.filledSlots < tournament.totalSlots,
                    testTag = "join_tournament_btn_${tournament.id}"
                )
            }
        }
    }
}

@Composable
fun AiBannerCallout(
    onExploreAi: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        borderColor = FireBluePrimary.copy(alpha = 0.6f),
        onClick = onExploreAi,
        testTag = "ai_banner_callout"
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(FireBlueDark, FlameOrange))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "S_SHORIF AI Intelligence Hub",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Event highlights caster, fake player detection & live meta guide.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = FireBluePrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun PlatformTrustCard(
    onRulesClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "⚡ S_SHORIF eSports Fair Play Guarantee",
                color = FireBluePrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• 100% Guaranteed prize payout via bKash, Nagad, and Rocket.\n• Advanced AI anti-cheat checks & device fingerprinting.\n• Verified custom rooms with real-time slot tracking.",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "📜 View Rules & Policy",
                    color = SkyBlueAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onRulesClick)
                )
                Text(
                    text = "💬 24/7 Support",
                    color = SkyBlueAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(onClick = onSupportClick)
                )
            }
        }
    }
}
