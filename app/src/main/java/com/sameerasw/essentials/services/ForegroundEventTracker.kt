package com.sameerasw.essentials.services

/** Selects current state from overlapping usage-event batches without replaying history. */
internal class ForegroundEventTracker {
    var packageName: String? = null
        private set
    private var timestamp = Long.MIN_VALUE

    fun resumed(packageName: String, timestamp: Long) {
        if (timestamp >= this.timestamp) {
            this.timestamp = timestamp
            this.packageName = packageName
        }
    }

    fun reset() {
        timestamp = Long.MIN_VALUE
        packageName = null
    }
}
