package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppNotification
import com.example.data.model.MapType
import com.example.data.model.MatchMode
import com.example.data.model.SlotRegistration
import com.example.data.model.Tournament
import com.example.data.model.UserProfile
import com.example.data.repository.AuthenticityReport
import com.example.data.repository.TournamentRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    TOURNAMENTS,
    TOURNAMENT_DETAILS,
    CREATE_TOURNAMENT,
    WALLET,
    LEADERBOARD,
    AI_HUB,
    PROFILE,
    ABOUT,
    SUPPORT
}

data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String = "Now"
)

class TournamentViewModel(
    private val repository: TournamentRepository = TournamentRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    private val _selectedTournamentId = MutableStateFlow<String?>("t1")
    val selectedTournamentId: StateFlow<String?> = _selectedTournamentId.asStateFlow()

    // Filters
    val selectedFilterMode = MutableStateFlow<MatchMode?>(null)
    val selectedFilterMap = MutableStateFlow<MapType?>(null)
    val searchQuery = MutableStateFlow("")

    val tournaments: StateFlow<List<Tournament>> = repository.tournaments
    val notifications: StateFlow<List<AppNotification>> = repository.notifications
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val registrations: StateFlow<List<SlotRegistration>> = repository.registrations

    // Filtered Tournaments
    val filteredTournaments: StateFlow<List<Tournament>> = combine(
        tournaments,
        selectedFilterMode,
        selectedFilterMap,
        searchQuery
    ) { list, mode, map, query ->
        list.filter { t ->
            val matchMode = mode == null || t.matchMode == mode
            val matchMap = map == null || t.mapType == map
            val matchQuery = query.isBlank() || t.title.contains(query, ignoreCase = true) || t.mapType.displayName.contains(query, ignoreCase = true)
            matchMode && matchMap && matchQuery
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Join Tournament Dialog State
    private val _joinDialogTournament = MutableStateFlow<Tournament?>(null)
    val joinDialogTournament: StateFlow<Tournament?> = _joinDialogTournament.asStateFlow()

    val joinIgn = MutableStateFlow("")
    val joinUid = MutableStateFlow("")
    val joinTeam = MutableStateFlow("")
    val joinPhone = MutableStateFlow("")
    val joinPaymentMethod = MutableStateFlow("bKash")
    val joinTrxId = MutableStateFlow("")
    val isJoining = MutableStateFlow(false)
    val joinSuccessMessage = MutableStateFlow<String?>(null)
    val joinErrorMessage = MutableStateFlow<String?>(null)

    // Notification Drawer
    val isNotificationDrawerOpen = MutableStateFlow(false)

    // Snack messages
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // AI Assistant Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("1", "👋 Welcome to S_SHORIF eSports AI Assistant! Ask me about custom room rules, bKash payments, Free Fire weapon meta, or anti-cheat reports.", false, "10:00 AM")
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    val chatInputText = MutableStateFlow("")
    val isAiThinking = MutableStateFlow(false)

    // AI Highlights
    val highlightsTournamentTitle = MutableStateFlow("")
    val highlightsOutput = MutableStateFlow<String?>(null)
    val isGeneratingHighlights = MutableStateFlow(false)

    // AI Anti Cheat
    val antiCheatIgn = MutableStateFlow("SHORIF_BOSS")
    val antiCheatUid = MutableStateFlow("5928194821")
    val antiCheatLevel = MutableStateFlow("65")
    val antiCheatKd = MutableStateFlow("5.6")
    val antiCheatReport = MutableStateFlow<AuthenticityReport?>(null)
    val isCheckingAntiCheat = MutableStateFlow(false)

    // Leaderboard Filter
    val leaderboardPeriod = MutableStateFlow("All Time")

    init {
        // Pre-fill profile IGN into join form
        viewModelScope.launch {
            userProfile.collect { profile ->
                if (joinIgn.value.isEmpty()) joinIgn.value = profile.ign
                if (joinUid.value.isEmpty()) joinUid.value = profile.uid
                if (joinPhone.value.isEmpty()) joinPhone.value = profile.phone
            }
        }
    }

    fun navigateTo(screen: AppScreen, tournamentId: String? = null) {
        if (tournamentId != null) {
            _selectedTournamentId.value = tournamentId
        }
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (_screenHistory.isNotEmpty()) {
            val prev = _screenHistory.removeAt(_screenHistory.lastIndex)
            _currentScreen.value = prev
            true
        } else if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            true
        } else {
            false
        }
    }

    fun openJoinDialog(tournament: Tournament) {
        _joinDialogTournament.value = tournament
        joinErrorMessage.value = null
        joinSuccessMessage.value = null
        joinTrxId.value = ""
    }

    fun closeJoinDialog() {
        _joinDialogTournament.value = null
        joinErrorMessage.value = null
        joinSuccessMessage.value = null
        isJoining.value = false
    }

    fun submitJoinTournament() {
        val t = _joinDialogTournament.value ?: return
        if (joinIgn.value.isBlank()) {
            joinErrorMessage.value = "Please enter your Free Fire In-Game Name (IGN)"
            return
        }
        if (joinUid.value.isBlank() || joinUid.value.length < 8) {
            joinErrorMessage.value = "Please enter a valid Free Fire Player UID (8-10 digits)"
            return
        }
        if (t.entryFee > 0 && joinTrxId.value.isBlank()) {
            joinErrorMessage.value = "Please enter payment Transaction ID (TrxID) for verification"
            return
        }

        viewModelScope.launch {
            isJoining.value = true
            delay(800) // Realistic server verification simulation

            val result = repository.joinTournament(
                tournamentId = t.id,
                ign = joinIgn.value.trim(),
                uid = joinUid.value.trim(),
                teamName = joinTeam.value.trim().takeIf { it.isNotBlank() },
                phone = joinPhone.value.trim(),
                paymentMethod = if (t.entryFee > 0) joinPaymentMethod.value else "Free Entry",
                transactionId = joinTrxId.value.trim()
            )

            isJoining.value = false
            result.onSuccess { reg ->
                joinSuccessMessage.value = "🎉 Registration Confirmed! You are assigned Slot #${reg.slotNumber}."
                _toastEvent.emit("Slot #${reg.slotNumber} Booked Successfully!")
            }.onFailure { err ->
                joinErrorMessage.value = err.message ?: "Failed to join tournament"
            }
        }
    }

    fun toggleReleaseRoom(tournamentId: String) {
        val released = repository.toggleReleaseRoom(tournamentId)
        viewModelScope.launch {
            _toastEvent.emit(if (released) "Room ID & Pass Published to Players! 📢" else "Room ID Hidden 🔒")
        }
    }

    fun createTournament(
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
    ) {
        val created = repository.createTournament(
            title = title,
            matchMode = matchMode,
            mapType = mapType,
            scheduleTime = scheduleTime,
            entryFee = entryFee,
            prizePool = prizePool,
            firstPrize = firstPrize,
            perKillBonus = perKillBonus,
            totalSlots = totalSlots,
            rules = rules
        )
        viewModelScope.launch {
            _toastEvent.emit("Tournament '${created.title}' Created Successfully! 🏆")
            navigateTo(AppScreen.TOURNAMENT_DETAILS, created.id)
        }
    }

    fun addDeposit(amount: Double, method: String, trxId: String) {
        repository.addWalletFunds(amount, method, trxId)
        viewModelScope.launch {
            _toastEvent.emit("৳$amount added to wallet balance successfully!")
        }
    }

    fun claimWithdraw(amount: Double, method: String, number: String) {
        val res = repository.claimPrize(amount, method, number)
        viewModelScope.launch {
            res.onSuccess {
                _toastEvent.emit(it)
            }.onFailure {
                _toastEvent.emit(it.message ?: "Withdrawal failed")
            }
        }
    }

    fun updateProfile(ign: String, uid: String, phone: String) {
        repository.updateProfile(ign, uid, phone)
        viewModelScope.launch {
            _toastEvent.emit("Profile Updated! 🎮")
        }
    }

    fun sendAiChatMessage() {
        val text = chatInputText.value.trim()
        if (text.isBlank()) return

        val userMsg = ChatMessage(System.currentTimeMillis().toString(), text, true)
        _chatMessages.update { it + userMsg }
        chatInputText.value = ""

        viewModelScope.launch {
            isAiThinking.value = true
            delay(600)
            val answer = repository.getAiAssistantAnswer(text)
            val aiMsg = ChatMessage((System.currentTimeMillis() + 1).toString(), answer, false)
            _chatMessages.update { it + aiMsg }
            isAiThinking.value = false
        }
    }

    fun generateEventHighlights(tournamentTitle: String) {
        highlightsTournamentTitle.value = tournamentTitle
        viewModelScope.launch {
            isGeneratingHighlights.value = true
            highlightsOutput.value = null
            delay(900)
            val result = repository.generateEventHighlights(tournamentTitle)
            highlightsOutput.value = result
            isGeneratingHighlights.value = false
        }
    }

    fun checkPlayerAntiCheat() {
        viewModelScope.launch {
            isCheckingAntiCheat.value = true
            antiCheatReport.value = null
            delay(1000)
            val lvl = antiCheatLevel.value.toIntOrNull() ?: 50
            val kd = antiCheatKd.value.toDoubleOrNull() ?: 4.0
            val report = repository.analyzePlayerAuthenticity(
                ign = antiCheatIgn.value,
                uid = antiCheatUid.value,
                reportedLevel = lvl,
                reportedKd = kd
            )
            antiCheatReport.value = report
            isCheckingAntiCheat.value = false
        }
    }

    fun getLeaderboard(period: String) = repository.getLeaderboard(period)

    fun markNotificationRead(id: String) {
        repository.markNotificationAsRead(id)
    }

    fun markAllNotificationsRead() {
        repository.markAllNotificationsAsRead()
    }
}
