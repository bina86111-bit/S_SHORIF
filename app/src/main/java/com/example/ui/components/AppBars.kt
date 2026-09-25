package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.SkyBlueAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

@Composable
fun AppTopBar(
    walletBalance: Double,
    unreadNotificationsCount: Int,
    onWalletClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    0.5.dp,
                    Brush.verticalGradient(listOf(BorderGlow.copy(alpha = 0.4f), Color.Transparent))
                )
            ),
        color = DeepNavyBg.copy(alpha = 0.95f),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.testTag("app_logo_row")
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(FireBlueDark, FireBluePrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚡",
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "S_SHORIF",
                        color = FireBluePrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "FREE FIRE ESPORTS",
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Quick Actions: Wallet + Notifications
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wallet Chip
                Box(
                    modifier = Modifier
                        .testTag("top_wallet_chip")
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardNavyElevated)
                        .border(0.5.dp, BorderGlow.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable(onClick = onWalletClick)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "৳",
                            color = FlameOrange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.0f", walletBalance),
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Notification Bell
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("notification_bell_button")
                        .clip(CircleShape)
                        .background(CardNavyElevated)
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationsCount > 0) {
                                Badge(
                                    containerColor = DangerRed,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = unreadNotificationsCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = FireBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

sealed class NavigationBarTab(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val testTag: String
) {
    object Home : NavigationBarTab(AppScreen.HOME, "Home", Icons.Default.Home, "nav_home")
    object Tournaments : NavigationBarTab(AppScreen.TOURNAMENTS, "Matches", Icons.Default.SportsEsports, "nav_tournaments")
    object AiHub : NavigationBarTab(AppScreen.AI_HUB, "S_SHORIF AI", Icons.Default.AutoAwesome, "nav_ai_hub")
    object Wallet : NavigationBarTab(AppScreen.WALLET, "Wallet", Icons.Default.AccountBalanceWallet, "nav_wallet")
    object Leaderboard : NavigationBarTab(AppScreen.LEADERBOARD, "Rankings", Icons.Default.EmojiEvents, "nav_leaderboard")
    object Profile : NavigationBarTab(AppScreen.PROFILE, "Profile", Icons.Default.Person, "nav_profile")
}

@Composable
fun AppBottomBar(
    currentScreen: AppScreen,
    onTabSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavigationBarTab.Home,
        NavigationBarTab.Tournaments,
        NavigationBarTab.AiHub,
        NavigationBarTab.Wallet,
        NavigationBarTab.Leaderboard,
        NavigationBarTab.Profile
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    0.5.dp,
                    Brush.verticalGradient(listOf(BorderSubtle, Color.Transparent))
                )
            ),
        color = DeepNavyBg.copy(alpha = 0.96f),
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen
                Column(
                    modifier = Modifier
                        .testTag(item.testTag)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(item.screen) }
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) FireBluePrimary.copy(alpha = 0.18f) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) FireBluePrimary else TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        color = if (isSelected) FireBluePrimary else TextMuted,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
