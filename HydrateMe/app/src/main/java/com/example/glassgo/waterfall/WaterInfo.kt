package com.example.glassgo.waterfall

import java.util.*

class WaterInfo(val storage: DataStorage) {
    fun addLiquid(id: Int, amountWithCoef: Int, amount: Int) {
        storage.addLiquid(amountWithCoef)
        storage.addDrink(id, amount)
    }

    fun getCurrentWater(): Int {
        return storage.getCurrentWater()
    }

    fun dayPassed(liquidNeeded: Float): Boolean {
        val currentTime = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
        if (storage.prevDayCall / 86400000L < currentTime / 86400000L) {
            val range = (currentTime / 86400000L - storage.prevDayCall / 86400000L).toInt()
            if (liquidNeeded <= storage.getCurrentWater() / 1000.0) {
                storage.dayInRow += 1
                storage.updateHighest()
            } else {
                storage.dayInRow = 0
            }
            if (range != 1) {
                storage.dayInRow = 0
            }
            storage.prevDayCall = currentTime
            for (i in 0 until range) {
                storage.addNewDay()
            }
            return true
        }
        return false
    }

    fun getDayInRow(): Int {
        return storage.dayInRow
    }

    fun getHighestScore(): Int {
        return storage.highestScore
    }

    fun getDrinkAmount(id: Int): Int {
        return storage.getDrinkAmount(id)
    }

    fun getChronology(): MutableList<Triple<Int, Int, Date>> {
        return storage.getDailyDrinkInfo()
    }

    fun getLastWeekStat(): List<Int> {
        return storage.getLastWeekStat()
    }

    fun reset() {
        storage.resetLastDay()
    }

    fun checkMonthUnusage(drinkId: Int): Int {
        return storage.checkMonthUnusage(drinkId)
    }
}