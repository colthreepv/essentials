package com.sameerasw.essentials.services

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ForegroundEventTrackerTest {
    @Test fun returningToLauncherReplacesProtectedApp() {
        val tracker = ForegroundEventTracker()
        tracker.resumed("wallet", 100)
        tracker.resumed("launcher", 200)
        assertEquals("launcher", tracker.packageName)
    }

    @Test fun overlappingHistoryDoesNotRewindCurrentState() {
        val tracker = ForegroundEventTracker()
        tracker.resumed("wallet", 100)
        tracker.resumed("launcher", 200)
        tracker.resumed("wallet", 100)
        assertEquals("launcher", tracker.packageName)
    }

    @Test fun delayedNewerEventIsAccepted() {
        val tracker = ForegroundEventTracker()
        tracker.resumed("wallet", 100)
        tracker.resumed("launcher", 150)
        assertEquals("launcher", tracker.packageName)
    }

    @Test fun sameTimestampPreservesEventOrder() {
        val tracker = ForegroundEventTracker()
        tracker.resumed("wallet", 100)
        tracker.resumed("launcher", 100)
        assertEquals("launcher", tracker.packageName)
    }

    @Test fun restartHistoryEndsInReopenedApp() {
        val tracker = ForegroundEventTracker()
        tracker.resumed("wallet", 100)
        tracker.resumed("launcher", 200)
        tracker.resumed("wallet", 300)
        assertEquals("wallet", tracker.packageName)
    }

    @Test fun clockCorrectionAllowsEarlierTimestampsAfterReset() {
        val tracker = ForegroundEventTracker()
        tracker.resumed("wallet", 1000)
        tracker.reset()
        assertNull(tracker.packageName)
        tracker.resumed("launcher", 100)
        assertEquals("launcher", tracker.packageName)
    }
}
