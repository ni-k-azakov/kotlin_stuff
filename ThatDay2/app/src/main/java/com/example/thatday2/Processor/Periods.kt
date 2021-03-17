package com.example.thatday2.Processor

import kotlin.math.roundToInt

class PeriodsInfo(val savedData: DataStorage) {
    val averagePeriodsDuration: Int
        get() {
            if (savedData.getPeriodDurInfo().size == 0) return 0
            var total: Long = 0
            for (period in savedData.getPeriodDurInfo()) {
                total += period
            }
            total /= savedData.getPeriodDurInfo().size
            return millisToDays(total)
        }

    val averageCycleDuration: Int
        get() {
            if (savedData.getCycleDurInfo().size == 0) return 0
            var total: Long = 0
            for (cycle in savedData.getCycleDurInfo()) {
                total += cycle
            }
            total /= savedData.getCycleDurInfo().size
            return millisToDays(total)
        }

    fun addOrRemoveDate(date: Long) {
        savedData.addOrRemoveDate(date)
    }
    fun updateStat() {
        savedData.updateDurations()
    }

    private fun millisToDays(millis: Long): Int {
        return (millis / 86400000F).roundToInt()
    }

    fun getPeriodDays(): MutableList<Long> {
        return savedData.periodDays
    }
}