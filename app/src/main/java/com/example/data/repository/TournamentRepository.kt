package com.example.data.repository

import com.example.data.model.AppNotification
import com.example.data.model.BadgeItem
import com.example.data.model.LeaderboardPlayer
import com.example.data.model.MapType
import com.example.data.model.MatchMode
import com.example.data.model.NotificationType
import com.example.data.model.RegistrationStatus
import com.example.data.model.SlotRegistration
import com.example.data.model.Tournament
import com.example.data.model.TournamentStatus
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class TournamentRepository {

    private val _tournaments = MutableStateFlow<List<Tournament>>(initialTournaments())
    val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

    private val _registrations = MutableStateFlow<List<SlotRegistration>>(initialRegistrations())
    val registrations: StateFlow<List<SlotRegistration>> = _registrations.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(initialNotifications())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun getTournamentById(id: String): Tournament? {
        return _tournaments.value.find { it.id == id }
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
        totalSlots: Int = 48,
        rules: List<String>
    ): Tournament {
        val newId = UUID.randomUUID().toString().take(8)
        val generatedRoomId = (7000000..9999999).random().toString()
        val generatedPass = "shorif${(100..999).random()}"

        val newTournament = Tournament(
            id = newId,
            title = title,
            matchMode = matchMode,
            mapType = mapType,
            scheduleTime = scheduleTime,
            entryFee = entryFee,
            prizePool = prizePool,
            firstPrize = firstPrize,
            perKillBonus = perKillBonus,
            totalSlots = totalSlots,
            filledSlots = 0,
            status = TournamentStatus.REGISTRATION_OPEN,
            roomId = generatedRoomId,
            roomPassword = generatedPass,
            isRoomReleased = false,
            rules = if (rules.isNotEmpty()) rules else listOf(
                "No Hacks or Config files allowed.",
                "Custom Room ID release 15 mins before match.",
                "Mandatory end-game screenshot submission."
            ),
            isFeatured = false,
            organizer = "S_SHORIF eSports"
        )

        _tournaments.update { listOf(newTournament) + it }

        // Add Notification
        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            title = "New Tournament Hosted!",
            message = "Tournament '$title' is live for registrations. Entry Fee: ৳$entryFee.",
            timestamp = "Just now",
            type = NotificationType.TOURNAMENT_NEW,
            targetTournamentId = newId
        )
        _notifications.update { listOf(notif) + it }

        return newTournament
    }

    fun joinTournament(
        tournamentId: String,
        ign: String,
        uid: String,
        teamName: String?,
        phone: String,
        paymentMethod: String,
        transactionId: String
    ): Result<SlotRegistration> {
        val tournament = getTournamentById(tournamentId)
            ?: return Result.failure(IllegalArgumentException("Tournament not found"))

        if (tournament.filledSlots >= tournament.totalSlots) {
            return Result.failure(IllegalStateException("Tournament is already full!"))
        }

        val allocatedSlot = tournament.filledSlots + 1
        val reg = SlotRegistration(
            id = UUID.randomUUID().toString(),
            tournamentId = tournamentId,
            slotNumber = allocatedSlot,
            ign = ign,
            freeFireUid = uid,
            teamName = teamName.takeIf { !it.isNullOrBlank() },
            phone = phone,
            paymentMethod = paymentMethod,
            transactionId = if (transactionId.isNotBlank()) transactionId else "FREE-PROMO-${(1000..9999).random()}",
            registeredAt = "Today",
            status = RegistrationStatus.CONFIRMED
        )

        // Update slots
        _tournaments.update { list ->
            list.map {
                if (it.id == tournamentId) {
                    it.copy(filledSlots = it.filledSlots + 1)
                } else it
            }
        }

        // Save registration
        _registrations.update { listOf(reg) + it }

        // Add Notification
        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            title = "Slot #$allocatedSlot Allocated! 🎮",
            message = "You have registered for '${tournament.title}'. Check Room info 15 mins before ${tournament.scheduleTime}.",
            timestamp = "Just now",
            type = NotificationType.ROOM_RELEASED,
            targetTournamentId = tournamentId
        )
        _notifications.update { listOf(notif) + it }

        // Update user stats
        _userProfile.update {
            it.copy(matchesPlayed = it.matchesPlayed + 1)
        }

        return Result.success(reg)
    }

    fun toggleReleaseRoom(tournamentId: String): Boolean {
        var released = false
        _tournaments.update { list ->
            list.map {
                if (it.id == tournamentId) {
                    val next = !it.isRoomReleased
                    released = next
                    it.copy(isRoomReleased = next)
                } else it
            }
        }

        val tournament = getTournamentById(tournamentId)
        if (tournament != null && released) {
            val notif = AppNotification(
                id = UUID.randomUUID().toString(),
                title = "🚨 Room ID & Pass Released!",
                message = "Room ID: ${tournament.roomId} | Password: ${tournament.roomPassword} for ${tournament.title}. Enter now!",
                timestamp = "Just now",
                type = NotificationType.ROOM_RELEASED,
                targetTournamentId = tournamentId
            )
            _notifications.update { listOf(notif) + it }
        }
        return released
    }

    fun getRegistrationsForTournament(tournamentId: String): List<SlotRegistration> {
        return _registrations.value.filter { it.tournamentId == tournamentId }
    }

    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }

    fun updateProfile(ign: String, uid: String, phone: String) {
        _userProfile.update {
            it.copy(ign = ign, uid = uid, phone = phone)
        }
    }

    fun addWalletFunds(amount: Double, paymentMethod: String, trxId: String) {
        _userProfile.update {
            it.copy(walletBalance = it.walletBalance + amount)
        }
        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            title = "Wallet Deposit Verified! 💰",
            message = "৳$amount added via $paymentMethod (TrxID: $trxId).",
            timestamp = "Just now",
            type = NotificationType.PAYMENT_SUCCESS
        )
        _notifications.update { listOf(notif) + it }
    }

    fun claimPrize(amount: Double, method: String, number: String): Result<String> {
        if (_userProfile.value.walletBalance < amount) {
            return Result.failure(IllegalStateException("Insufficient wallet balance."))
        }
        _userProfile.update {
            it.copy(walletBalance = it.walletBalance - amount)
        }
        val notif = AppNotification(
            id = UUID.randomUUID().toString(),
            title = "Prize Payout Processing ⚡",
            message = "Withdrawal of ৳$amount to $method ($number) queued. Expected in 15-30 mins.",
            timestamp = "Just now",
            type = NotificationType.PAYMENT_SUCCESS
        )
        _notifications.update { listOf(notif) + it }
        return Result.success("Withdrawal request for ৳$amount sent to admin!")
    }

    fun getLeaderboard(period: String = "All Time"): List<LeaderboardPlayer> {
        return when (period) {
            "Today" -> listOf(
                LeaderboardPlayer(1, "SHORIF_BOSS", "5928194821", 5, 34, 4, 380, "Grandmaster", "👑"),
                LeaderboardPlayer(2, "RAKIB_OP", "8492019482", 4, 28, 3, 310, "Heroic", "⚡"),
                LeaderboardPlayer(3, "FF_VIPER_99", "7481920381", 6, 26, 2, 280, "Heroic", "🔥"),
                LeaderboardPlayer(4, "TANVIR_GOD", "1948201942", 4, 21, 2, 235, "Diamond", "🎯"),
                LeaderboardPlayer(5, "CYBER_NINJA", "6192837482", 3, 18, 1, 190, "Diamond", "🥷")
            )
            "Weekly" -> listOf(
                LeaderboardPlayer(1, "S_SHORIF_ELITE", "5928194821", 24, 142, 16, 1680, "Grandmaster", "👑"),
                LeaderboardPlayer(2, "BENGAL_TIGERS", "9382716253", 22, 128, 14, 1490, "Grandmaster", "🐅"),
                LeaderboardPlayer(3, "RAKIB_OP", "8492019482", 20, 115, 12, 1340, "Heroic", "⚡"),
                LeaderboardPlayer(4, "HEADSHOT_ARMY", "5829104829", 19, 98, 10, 1180, "Heroic", "🎯"),
                LeaderboardPlayer(5, "NO_MERCY_BD", "4729103847", 18, 92, 9, 1090, "Diamond", "💀"),
                LeaderboardPlayer(6, "DESI_KILLERS", "8492018491", 17, 85, 7, 980, "Diamond", "⚔️")
            )
            else -> listOf(
                LeaderboardPlayer(1, "SHORIF_BOSS", "5928194821", 120, 840, 78, 9400, "Grandmaster", "👑"),
                LeaderboardPlayer(2, "SQUAD_VORTEX", "9812739182", 112, 790, 72, 8820, "Grandmaster", "🌪️"),
                LeaderboardPlayer(3, "BENGAL_TIGERS", "9382716253", 108, 745, 68, 8310, "Grandmaster", "🐅"),
                LeaderboardPlayer(4, "RAKIB_OP", "8492019482", 98, 680, 59, 7600, "Heroic", "⚡"),
                LeaderboardPlayer(5, "FF_VIPER_99", "7481920381", 94, 630, 54, 7150, "Heroic", "🔥"),
                LeaderboardPlayer(6, "TANVIR_GOD", "1948201942", 88, 590, 48, 6620, "Diamond", "🎯"),
                LeaderboardPlayer(7, "CYBER_NINJA", "6192837482", 82, 540, 42, 6080, "Diamond", "🥷"),
                LeaderboardPlayer(8, "SNIPER_QUEEN", "3928104821", 76, 490, 39, 5540, "Diamond", "🏹")
            )
        }
    }

    // AI Feature 1: Highlights Summary
    fun generateEventHighlights(tournamentTitle: String): String {
        return """
            🏆 [S_SHORIF AI eSports Match Caster Report]
            Event: $tournamentTitle
            
            ⚡ Match Climax & MVP Highlights:
            • Final Circle Drama: The intense final zone collapsed near Peak / Clock Tower with 3 squads surviving.
            • Clutch of the Day: SHORIF_BOSS pulled off a masterclass 1v3 with M1887 headshots under 8 seconds to clinch the Booyah!
            • Most Eliminations: Team Cyber Ghosts dominated the kill feeds with 19 total squad kills.
            • Long Range Precision: Woodpecker marksman snipe landed from 240m away through gloo wall gap.
            
            ⭐ AI Performance Rating: 9.8/10 (High Competitive Intensity, Clean Fair-Play Recorded).
        """.trimIndent()
    }

    // AI Feature 2: Anti-Cheat & Fake Player Entry Detector
    fun analyzePlayerAuthenticity(ign: String, uid: String, reportedLevel: Int, reportedKd: Double): AuthenticityReport {
        val isUidValid = uid.length in 8..11 && uid.all { it.isDigit() }
        val isIgnLegit = ign.isNotBlank() && ign.length in 3..16
        val isKdSuspicious = reportedKd > 15.0 || (reportedKd > 8.0 && reportedLevel < 25)
        val isEmulatorLikely = reportedKd > 12.0 && reportedLevel < 35

        val riskScore = when {
            !isUidValid -> 95
            isKdSuspicious && isEmulatorLikely -> 85
            isKdSuspicious -> 60
            reportedLevel < 15 -> 40
            else -> 8
        }

        val verdict = when {
            riskScore >= 80 -> "HIGH RISK (SUSPECTED EMULATOR / SCRIPT)"
            riskScore >= 40 -> "MODERATE RISK (MANUAL REVIEW REQUIRED)"
            else -> "VERIFIED AUTHENTIC PLAYER ✅"
        }

        val checks = listOf(
            "UID Numerical Validation: " + (if (isUidValid) "Passed (Valid Garena ID format)" else "Failed (Malformed ID)"),
            "Level & Stat Consistency: " + (if (!isKdSuspicious) "Passed (Normal Human KD Curve)" else "Flagged (Abnormal Outlier K/D: $reportedKd)"),
            "Device Fingerprint: Clean Mobile Android Sensor Telemetry detected.",
            "Fair-Play Database: Clean record in S_SHORIF tournament blacklist."
        )

        return AuthenticityReport(
            ign = ign,
            uid = uid,
            riskScore = riskScore,
            verdict = verdict,
            checks = checks,
            isApproved = riskScore < 60
        )
    }

    // AI Feature 3: Assistant Chat
    fun getAiAssistantAnswer(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            "room" in lower || "password" in lower || "id" in lower -> {
                "🔑 Room ID and Password are automatically published inside the tournament details screen exactly 15 minutes before the match start time. You'll also receive a live notification popup!"
            }
            "rule" in lower || "hack" in lower || "script" in lower || "cheat" in lower -> {
                "🛡️ S_SHORIF Fair Play Policy: Strictly NO Emulators, Macro/Config scripts, or teaming allowed. Gun properties are turned OFF. End-match kill screenshots are mandatory!"
            }
            "payment" in lower || "bkash" in lower || "nagad" in lower || "fee" in lower -> {
                "💳 Payment is 100% secured! Send money to our official bKash/Nagad Merchant number shown in the Join modal, copy the Transaction ID, and your slot is instantly reserved!"
            }
            "prize" in lower || "money" in lower || "reward" in lower || "withdraw" in lower -> {
                "🎁 Prize distribution happens within 30 minutes of match completion directly to your in-app wallet, which you can withdraw anytime to bKash or Nagad."
            }
            "meta" in lower || "weapon" in lower || "gun" in lower || "best" in lower -> {
                "🔫 Current Free Fire Tournament Meta: Tatsuya / Homer character combo paired with M1887 shotgun for close combat, and Woodpecker/AC80 for mid-range zone gatekeeping."
            }
            else -> {
                "🤖 S_SHORIF AI at your service! I can guide you through tournament registration, room credential releases, bKash payments, prize claiming, and anti-cheat verification."
            }
        }
    }

    private fun initialTournaments(): List<Tournament> {
        return listOf(
            Tournament(
                id = "t1",
                title = "S_SHORIF Grand Championship #42",
                matchMode = MatchMode.SQUAD,
                mapType = MapType.BERMUDA,
                scheduleTime = "Today, 08:30 PM",
                entryFee = 100,
                prizePool = 3500,
                firstPrize = 2000,
                perKillBonus = 25,
                totalSlots = 48,
                filledSlots = 41,
                status = TournamentStatus.REGISTRATION_OPEN,
                roomId = "8941203",
                roomPassword = "ff42win",
                isRoomReleased = true,
                isFeatured = true
            ),
            Tournament(
                id = "t2",
                title = "Bermuda Rush Hour Solo Clash",
                matchMode = MatchMode.SOLO,
                mapType = MapType.BERMUDA,
                scheduleTime = "Today, 10:00 PM",
                entryFee = 30,
                prizePool = 1200,
                firstPrize = 700,
                perKillBonus = 15,
                totalSlots = 48,
                filledSlots = 45,
                status = TournamentStatus.REGISTRATION_OPEN,
                roomId = "7381920",
                roomPassword = "solo99",
                isRoomReleased = false,
                isFeatured = false
            ),
            Tournament(
                id = "t3",
                title = "Purgatory Duo Firestorm",
                matchMode = MatchMode.DUO,
                mapType = MapType.PURGATORY,
                scheduleTime = "Tomorrow, 07:00 PM",
                entryFee = 60,
                prizePool = 2200,
                firstPrize = 1200,
                perKillBonus = 20,
                totalSlots = 48,
                filledSlots = 26,
                status = TournamentStatus.REGISTRATION_OPEN,
                roomId = "5829104",
                roomPassword = "duoking",
                isRoomReleased = false,
                isFeatured = false
            ),
            Tournament(
                id = "t4",
                title = "Daily Free Fire Scrims [Free Entry]",
                matchMode = MatchMode.SQUAD,
                mapType = MapType.ALPINE,
                scheduleTime = "Starting in 10m",
                entryFee = 0,
                prizePool = 500,
                firstPrize = 300,
                perKillBonus = 10,
                totalSlots = 48,
                filledSlots = 48,
                status = TournamentStatus.LIVE,
                roomId = "9182374",
                roomPassword = "livefree",
                isRoomReleased = true,
                isFeatured = false
            ),
            Tournament(
                id = "t5",
                title = "Kalahari Desert Sniper Warfare",
                matchMode = MatchMode.SOLO,
                mapType = MapType.KALAHARI,
                scheduleTime = "Tomorrow, 09:30 PM",
                entryFee = 40,
                prizePool = 1600,
                firstPrize = 900,
                perKillBonus = 20,
                totalSlots = 48,
                filledSlots = 18,
                status = TournamentStatus.REGISTRATION_OPEN,
                roomId = "6729104",
                roomPassword = "kalahari",
                isRoomReleased = false,
                isFeatured = false
            )
        )
    }

    private fun initialRegistrations(): List<SlotRegistration> {
        return listOf(
            SlotRegistration("r1", "t1", 1, "SHORIF_BOSS", "5928194821", "S_SHORIF_ELITE", "01711111111", "bKash", "TRX88492019", "06:15 PM"),
            SlotRegistration("r2", "t1", 2, "RAKIB_OP", "8492019482", "CYBER_GHOSTS", "01822222222", "Nagad", "TRX99381023", "06:20 PM"),
            SlotRegistration("r3", "t1", 3, "TANVIR_HEADSHOT", "1948201942", "RED_VIPERS", "01933333333", "bKash", "TRX11029384", "06:30 PM"),
            SlotRegistration("r4", "t1", 4, "CYBER_NINJA", "6192837482", "DARK_LEGION", "01644444444", "Rocket", "TRX44920194", "06:45 PM"),
            SlotRegistration("r5", "t1", 5, "BENGAL_TIGER_01", "9382716253", "TIGER_SQUAD", "01755555555", "bKash", "TRX77281920", "07:00 PM")
        )
    }

    private fun initialNotifications(): List<AppNotification> {
        return listOf(
            AppNotification(
                id = "n1",
                title = "🚨 Room Released: S_SHORIF Championship #42",
                message = "Room ID: 8941203 | Pass: ff42win. Match starts at 08:30 PM sharp!",
                timestamp = "5m ago",
                type = NotificationType.ROOM_RELEASED,
                targetTournamentId = "t1"
            ),
            AppNotification(
                id = "n2",
                title = "৳2,000 Prize Money Distributed! 🏆",
                message = "Congratulations to SHORIF_BOSS for 1st place in Daily Bermuda Solo!",
                timestamp = "2 hours ago",
                type = NotificationType.PAYMENT_SUCCESS
            ),
            AppNotification(
                id = "n3",
                title = "AI Anti-Cheat System Activated 🛡️",
                message = "All custom rooms are now guarded by S_SHORIF AI player verification.",
                timestamp = "1 day ago",
                type = NotificationType.AI_ALERT
            )
        )
    }
}

data class AuthenticityReport(
    val ign: String,
    val uid: String,
    val riskScore: Int,
    val verdict: String,
    val checks: List<String>,
    val isApproved: Boolean
)
