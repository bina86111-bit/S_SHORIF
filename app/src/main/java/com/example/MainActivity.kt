package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.NotificationBottomSheet
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AiHubScreen
import com.example.ui.screens.CreateTournamentScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinTournamentDialog
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.screens.TournamentDetailScreen
import com.example.ui.screens.TournamentsScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TournamentViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: TournamentViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current

                // Toast event listener
                LaunchedEffect(Unit) {
                    viewModel.toastEvent.collectLatest { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }

                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                val selectedTournamentId by viewModel.selectedTournamentId.collectAsStateWithLifecycle()
                val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
                val filteredTournaments by viewModel.filteredTournaments.collectAsStateWithLifecycle()
                val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
                val notifications by viewModel.notifications.collectAsStateWithLifecycle()
                val registrations by viewModel.registrations.collectAsStateWithLifecycle()

                val selectedFilterMode by viewModel.selectedFilterMode.collectAsStateWithLifecycle()
                val selectedFilterMap by viewModel.selectedFilterMap.collectAsStateWithLifecycle()
                val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

                val joinTournament by viewModel.joinDialogTournament.collectAsStateWithLifecycle()
                val joinIgn by viewModel.joinIgn.collectAsStateWithLifecycle()
                val joinUid by viewModel.joinUid.collectAsStateWithLifecycle()
                val joinTeam by viewModel.joinTeam.collectAsStateWithLifecycle()
                val joinPhone by viewModel.joinPhone.collectAsStateWithLifecycle()
                val joinPaymentMethod by viewModel.joinPaymentMethod.collectAsStateWithLifecycle()
                val joinTrxId by viewModel.joinTrxId.collectAsStateWithLifecycle()
                val isJoining by viewModel.isJoining.collectAsStateWithLifecycle()
                val joinSuccessMessage by viewModel.joinSuccessMessage.collectAsStateWithLifecycle()
                val joinErrorMessage by viewModel.joinErrorMessage.collectAsStateWithLifecycle()

                val isNotificationDrawerOpen by viewModel.isNotificationDrawerOpen.collectAsStateWithLifecycle()
                val unreadCount = notifications.count { !it.isRead }

                val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
                val chatInput by viewModel.chatInputText.collectAsStateWithLifecycle()
                val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()

                val highlightsOutput by viewModel.highlightsOutput.collectAsStateWithLifecycle()
                val isGeneratingHighlights by viewModel.isGeneratingHighlights.collectAsStateWithLifecycle()

                val antiCheatIgn by viewModel.antiCheatIgn.collectAsStateWithLifecycle()
                val antiCheatUid by viewModel.antiCheatUid.collectAsStateWithLifecycle()
                val antiCheatLevel by viewModel.antiCheatLevel.collectAsStateWithLifecycle()
                val antiCheatKd by viewModel.antiCheatKd.collectAsStateWithLifecycle()
                val antiCheatReport by viewModel.antiCheatReport.collectAsStateWithLifecycle()
                val isCheckingAntiCheat by viewModel.isCheckingAntiCheat.collectAsStateWithLifecycle()

                val leaderboardPeriod by viewModel.leaderboardPeriod.collectAsStateWithLifecycle()
                val leaderboardPlayers = viewModel.getLeaderboard(leaderboardPeriod)

                val activeTournament = tournaments.find { it.id == selectedTournamentId }
                    ?: tournaments.firstOrNull()

                // Root Scaffold
                Scaffold(
                    topBar = {
                        AppTopBar(
                            walletBalance = userProfile.walletBalance,
                            unreadNotificationsCount = unreadCount,
                            onWalletClick = { viewModel.navigateTo(AppScreen.WALLET, null) },
                            onNotificationsClick = { viewModel.isNotificationDrawerOpen.value = true }
                        )
                    },
                    bottomBar = {
                        AppBottomBar(
                            currentScreen = currentScreen,
                            onTabSelected = { screen -> viewModel.navigateTo(screen, null) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "screen_transition"
                        ) { screen ->
                            when (screen) {
                                AppScreen.HOME -> {
                                    HomeScreen(
                                        tournaments = filteredTournaments,
                                        selectedMode = selectedFilterMode,
                                        onSelectMode = { viewModel.selectedFilterMode.value = it },
                                        onNavigateTo = { target, id -> viewModel.navigateTo(target, id) },
                                        onJoinTournament = { t -> viewModel.openJoinDialog(t) }
                                    )
                                }
                                AppScreen.TOURNAMENTS -> {
                                    TournamentsScreen(
                                        tournaments = filteredTournaments,
                                        searchQuery = searchQuery,
                                        onSearchChange = { viewModel.searchQuery.value = it },
                                        selectedMode = selectedFilterMode,
                                        onSelectMode = { viewModel.selectedFilterMode.value = it },
                                        selectedMap = selectedFilterMap,
                                        onSelectMap = { viewModel.selectedFilterMap.value = it },
                                        onNavigateTo = { target, id -> viewModel.navigateTo(target, id) },
                                        onJoinTournament = { t -> viewModel.openJoinDialog(t) }
                                    )
                                }
                                AppScreen.TOURNAMENT_DETAILS -> {
                                    TournamentDetailScreen(
                                        tournament = activeTournament,
                                        registrations = registrations.filter { it.tournamentId == activeTournament?.id },
                                        onBack = { viewModel.navigateBack() },
                                        onJoinClick = { t -> viewModel.openJoinDialog(t) },
                                        onToggleRoomRelease = { id -> viewModel.toggleReleaseRoom(id) },
                                        onGenerateHighlights = { title -> viewModel.generateEventHighlights(title) },
                                        highlightsOutput = highlightsOutput,
                                        isGeneratingHighlights = isGeneratingHighlights
                                    )
                                }
                                AppScreen.CREATE_TOURNAMENT -> {
                                    CreateTournamentScreen(
                                        onBack = { viewModel.navigateBack() },
                                        onCreateTournament = { title, mode, map, time, fee, prize, first, kill, slots, rules ->
                                            viewModel.createTournament(title, mode, map, time, fee, prize, first, kill, slots, rules)
                                        }
                                    )
                                }
                                AppScreen.WALLET -> {
                                    WalletScreen(
                                        walletBalance = userProfile.walletBalance,
                                        onDeposit = { amount, method, trxId ->
                                            viewModel.addDeposit(amount, method, trxId)
                                        },
                                        onWithdraw = { amount, method, number ->
                                            viewModel.claimWithdraw(amount, method, number)
                                        },
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                                AppScreen.LEADERBOARD -> {
                                    LeaderboardScreen(
                                        players = leaderboardPlayers,
                                        selectedPeriod = leaderboardPeriod,
                                        onPeriodChange = { viewModel.leaderboardPeriod.value = it },
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                                AppScreen.AI_HUB -> {
                                    AiHubScreen(
                                        chatMessages = chatMessages,
                                        chatInput = chatInput,
                                        onChatInputChange = { viewModel.chatInputText.value = it },
                                        onSendMessage = { viewModel.sendAiChatMessage() },
                                        isAiThinking = isAiThinking,
                                        tournaments = tournaments,
                                        highlightsOutput = highlightsOutput,
                                        isGeneratingHighlights = isGeneratingHighlights,
                                        onGenerateHighlights = { title -> viewModel.generateEventHighlights(title) },
                                        antiCheatIgn = antiCheatIgn,
                                        onAntiCheatIgnChange = { viewModel.antiCheatIgn.value = it },
                                        antiCheatUid = antiCheatUid,
                                        onAntiCheatUidChange = { viewModel.antiCheatUid.value = it },
                                        antiCheatLevel = antiCheatLevel,
                                        onAntiCheatLevelChange = { viewModel.antiCheatLevel.value = it },
                                        antiCheatKd = antiCheatKd,
                                        onAntiCheatKdChange = { viewModel.antiCheatKd.value = it },
                                        antiCheatReport = antiCheatReport,
                                        isCheckingAntiCheat = isCheckingAntiCheat,
                                        onRunAntiCheatCheck = { viewModel.checkPlayerAntiCheat() },
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                                AppScreen.PROFILE -> {
                                    ProfileScreen(
                                        userProfile = userProfile,
                                        registrations = registrations,
                                        tournaments = tournaments,
                                        onUpdateProfile = { ign, uid, phone ->
                                            viewModel.updateProfile(ign, uid, phone)
                                        },
                                        onNavigateTo = { target, id -> viewModel.navigateTo(target, id) },
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                                AppScreen.ABOUT -> {
                                    AboutScreen(
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                                AppScreen.SUPPORT -> {
                                    SupportScreen(
                                        onNavigateToAiChat = {
                                            viewModel.navigateTo(AppScreen.AI_HUB, null)
                                        },
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                            }
                        }
                    }

                    // Join Tournament Dialog
                    joinTournament?.let { tournament ->
                        JoinTournamentDialog(
                            tournament = tournament,
                            ign = joinIgn,
                            onIgnChange = { viewModel.joinIgn.value = it },
                            uid = joinUid,
                            onUidChange = { viewModel.joinUid.value = it },
                            team = joinTeam,
                            onTeamChange = { viewModel.joinTeam.value = it },
                            phone = joinPhone,
                            onPhoneChange = { viewModel.joinPhone.value = it },
                            paymentMethod = joinPaymentMethod,
                            onPaymentMethodChange = { viewModel.joinPaymentMethod.value = it },
                            trxId = joinTrxId,
                            onTrxIdChange = { viewModel.joinTrxId.value = it },
                            isLoading = isJoining,
                            errorMessage = joinErrorMessage,
                            successMessage = joinSuccessMessage,
                            onDismiss = { viewModel.closeJoinDialog() },
                            onSubmit = { viewModel.submitJoinTournament() }
                        )
                    }

                    // Notification Bottom Sheet
                    if (isNotificationDrawerOpen) {
                        NotificationBottomSheet(
                            notifications = notifications,
                            onDismiss = { viewModel.isNotificationDrawerOpen.value = false },
                            onNotificationClick = { notif ->
                                viewModel.markNotificationRead(notif.id)
                                viewModel.isNotificationDrawerOpen.value = false
                                if (notif.targetTournamentId != null) {
                                    viewModel.navigateTo(AppScreen.TOURNAMENT_DETAILS, notif.targetTournamentId)
                                }
                            },
                            onMarkAllRead = { viewModel.markAllNotificationsRead() }
                        )
                    }
                }
            }
        }
    }
}
