package com.example.thatday2.Processor

class PeriodsInfo {
    val averagePeriodsDuration: Int
        get() {
            return 0
            var total: Long = 0
            for (period in savedData.getPeriodDurInfo()) {
                total += period
            }
            total /= savedData.getPeriodDurInfo().size
            return millisToDays(total)
        }
    val averageCycleDuration: Int
        get() {
            return 0
            var total: Long = 0
            for (cycle in savedData.getCycleDurInfo()) {
                total += cycle
            }
            total /= savedData.getCycleDurInfo().size
            return millisToDays(total)
        }
    val savedData: DataStorage = DataStorage()

    fun addDate(date: Long) {
        savedData.addDate(date)
    }
    fun deleteDay(date: Long) {
        savedData.deleteDate(date)
    }
    fun updateStat() {
        savedData.updateDurations()
    }

    private fun millisToDays(millis: Long): Int {
        return (millis / 86400000).toInt()
    }
}