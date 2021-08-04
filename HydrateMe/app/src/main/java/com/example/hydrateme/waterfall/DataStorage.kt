package com.example.hydrateme.waterfall

import java.io.Serializable

class DataStorage : Serializable {
    private val serialVersionUID = 1L

    var prevDayCall: Long = System.currentTimeMillis() - 86400000L * 7
    var dayInRow: Int = 0
    var highestScore: Int = 0
    private val drinks: MutableMap<Int, Int> = mutableMapOf()
    val drinksAllTime: MutableMap<Int, Map<Int, Int>> = mutableMapOf()
    var waterStat = mutableListOf<Int>(0)
    fun addLiquid(amount: Int) {
        waterStat[waterStat.lastIndex] += amount
    }

    fun getCurrentWater(): Int {
        return waterStat.last()
    }

    fun clearTodayDrinks() {
        drinks.clear()
    }

    fun addDrink(id: Int, amount: Int) {
        drinks[id] = drinks[id]?.plus(amount) ?: amount
    }

    fun addNewDay() {
        waterStat.add(0)
    }

    fun updateHighest() {
        if (dayInRow > highestScore) {
            highestScore = dayInRow
        }
    }

    fun getDrinkAmount(id: Int): Int {
        return if (drinks.containsKey(id)) {
            drinks[id]!!
        } else {
            0
        }
    }

    fun getLastWeekStat(): List<Int> {
        val stat = mutableListOf<Int>()
        for (i in 0 until 7) {
            stat.add(waterStat[waterStat.lastIndex - i])
        }
        return stat
    }
}