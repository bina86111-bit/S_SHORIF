package com.example.data.model

enum class MatchMode(val displayName: String, val teamSize: Int) {
    SOLO("Solo", 1),
    DUO("Duo", 2),
    SQUAD("Squad", 4)
}

enum class MapType(val displayName: String) {
    BERMUDA("Bermuda"),
    PURGATORY("Purgatory"),
    KALAHARI("Kalahari"),
    ALPINE("Alpine")
}

enum class TournamentStatus(val label: String) {
    REGISTRATION_OPEN("Registration Open"),
    UPCOMING("Upcoming"),
    LIVE("LIVE NOW"),
    COMPLETED("Finished")
}

data class Tournament(
    val id: String,
    val title: String,
    val matchMode: MatchMode,
    val mapType: MapType,
    val scheduleTime: String,
    val entryFee: Int,
    val prizePool: Int,
    val firstPrize: Int,
    val perKillBonus: Int,
    val totalSlots: Int = 48,
    val filledSlots: Int = 0,
    val status: TournamentStatus = TournamentStatus.REGISTRATION_OPEN,
    val roomId: String = "",
    val roomPassword: String = "",
    val isRoomReleased: Boolean = false,
    val rules: List<String> = listOf(
        "No Emulator or Script / Config allowed. Auto-kick on detection.",
        "Team teaming or match-fixing leads to permanent ban & prize forfeit.",
        "Join custom room 10 minutes prior to scheduled start time.",
        "Screenshots of victory and final kill count mandatory for reward claim.",
        "Gun properties are DISABLED for pure competitive eSports fair play."
    ),
    val isFeatured: Boolean = false,
    val organizer: String = "S_SHORIF eSports"
)

enum class RegistrationStatus {
    CONFIRMED,
    PENDING_VERIFICATION,
    REJECTED
}

data class SlotRegistration(
    val id: String,
    val tournamentId: String,
    val slotNumber: Int,
    val ign: String,
    val freeFireUid: String,
    val teamName: String? = null,
    val phone: String,
    val paymentMethod: String,
    val transactionId: String,
    val registeredAt: String,
    val status: RegistrationStatus = RegistrationStatus.CONFIRMED
)

data class LeaderboardPlayer(
    val rank: Int,
    val ign: String,
    val uid: String,
    val matches: Int,
    val kills: Int,
    val wins: Int,
    val points: Int,
    val tier: String,
    val avatarEmoji: String
)

enum class NotificationType {
    ROOM_RELEASED,
    MATCH_ALERT,
    PAYMENT_SUCCESS,
    TOURNAMENT_NEW,
    AI_ALERT
}

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val targetTournamentId: String? = null
)

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val isUnlocked: Boolean
)

data class UserProfile(
    val ign: String = "SHORIF_BOSS",
    val uid: String = "5928194821",
    val rank: String = "Grandmaster",
    val walletBalance: Double = 850.0,
    val matchesPlayed: Int = 42,
    val totalWins: Int = 28,
    val totalKills: Int = 214,
    val kdRatio: Double = 5.62,
    val headshotRate: Int = 68,
    val phone: String = "017XXXXXXXX",
    val badges: List<BadgeItem> = listOf(
        BadgeItem("1", "Grandmaster", "Reached top 100 in regional tournaments", "👑", true),
        BadgeItem("2", "Headshot King", "Maintained 60%+ headshot accuracy in 10 matches", "🎯", true),
        BadgeItem("3", "Booyah Master", "Won 25+ eSports tournament matches", "🏆", true),
        BadgeItem("4", "S_SHORIF Pro", "Verified competitive tournament player", "⚡", true),
        BadgeItem("5", "Lone Survivor", "Clutched a 1v4 in Bermuda Squad Final", "🛡️", false)
    )
)
