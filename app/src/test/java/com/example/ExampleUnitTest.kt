package com.example

import com.example.data.model.MapType
import com.example.data.model.MatchMode
import com.example.data.repository.TournamentRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: TournamentRepository

    @Before
    fun setUp() {
        repository = TournamentRepository()
    }

    @Test
    fun testInitialTournamentsPopulated() {
        val tournaments = repository.tournaments.value
        assertTrue("Tournaments should not be empty", tournaments.isNotEmpty())
        val featured = tournaments.find { it.isFeatured }
        assertNotNull("Featured tournament should exist", featured)
    }

    @Test
    fun testJoinTournamentAutoAllocatesSlot() {
        val tourney = repository.tournaments.value.first()
        val initialFilled = tourney.filledSlots
        val result = repository.joinTournament(
            tournamentId = tourney.id,
            ign = "TEST_SNIPER",
            uid = "1829304918",
            teamName = "ALPHA_SQUAD",
            phone = "01700000000",
            paymentMethod = "bKash",
            transactionId = "TRX123456"
        )

        assertTrue("Join should succeed", result.isSuccess)
        val reg = result.getOrNull()
        assertNotNull(reg)
        assertEquals(initialFilled + 1, reg!!.slotNumber)

        val updatedTourney = repository.getTournamentById(tourney.id)
        assertNotNull(updatedTourney)
        assertEquals(initialFilled + 1, updatedTourney!!.filledSlots)
    }

    @Test
    fun testToggleRoomRelease() {
        val tourney = repository.tournaments.value.find { !it.isRoomReleased }
        assertNotNull(tourney)
        val released = repository.toggleReleaseRoom(tourney!!.id)
        assertTrue("Room should now be released", released)

        val updated = repository.getTournamentById(tourney.id)
        assertTrue(updated!!.isRoomReleased)
    }

    @Test
    fun testAiAntiCheatDetection() {
        val authentic = repository.analyzePlayerAuthenticity("PRO_PLAYER", "5928194821", 60, 4.5)
        assertTrue("Normal stats should be approved", authentic.isApproved)
        assertTrue("Risk score should be low", authentic.riskScore < 30)

        val cheater = repository.analyzePlayerAuthenticity("HACKER_99", "123", 5, 25.0)
        assertFalse("Abnormal KD and invalid UID should be flagged", cheater.isApproved)
        assertTrue("Risk score should be high", cheater.riskScore >= 60)
    }

    @Test
    fun testWalletBalanceOperations() {
        val initialBalance = repository.userProfile.value.walletBalance
        repository.addWalletFunds(200.0, "Nagad", "TRX_TEST")
        assertEquals(initialBalance + 200.0, repository.userProfile.value.walletBalance, 0.01)

        val claimResult = repository.claimPrize(100.0, "bKash", "01711223344")
        assertTrue(claimResult.isSuccess)
        assertEquals(initialBalance + 100.0, repository.userProfile.value.walletBalance, 0.01)
    }
}
