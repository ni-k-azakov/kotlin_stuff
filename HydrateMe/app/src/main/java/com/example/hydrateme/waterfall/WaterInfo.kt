package com.example.hydrateme.waterfall

class WaterInfo(val storage: DataStorage) {
    fun addLiquid(id: Int, amountWithCoef: Int, amount: Int) {
        storage.addLiquid(amountWithCoef)
        storage.addDrink(id, amount)
    }

    fun getCurrentWater(): Int {
        return storage.getCurrentWater()
    }

    fun dayPassed(liquidNeeded: Double): Boolean {
        val currentTime = System.currentTimeMillis()
        if (storage.prevDayCall / 86400000L < currentTime / 86400000L) {
            val range = (currentTime / 86400000L - storage.prevDayCall / 86400000L).toInt()
            println(range)
            if (range == 1 && liquidNeeded <= storage.getCurrentWater() / 1000.0) {
                storage.dayInRow += 1
            } else {
                storage.updateHighest()
                storage.dayInRow = 0
            }
            storage.prevDayCall = System.currentTimeMillis()
            for (i in 0 until range) {
                storage.addNewDay()
            }
            storage.clearTodayDrinks()
            if (storage.dayInRow > storage.highestScore) {
                storage.highestScore = storage.dayInRow
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

    fun getLastWeekStat(): List<Int> {
        return storage.getLastWeekStat()
    }
}