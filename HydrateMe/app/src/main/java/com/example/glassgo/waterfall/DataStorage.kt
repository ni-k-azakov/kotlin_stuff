package com.example.glassgo.waterfall

import java.io.Serializable
import java.util.*

class DataStorage : Serializable {
    private val serialVersionUID = 1L

    var prevDayCall: Long = System.currentTimeMillis() - 86400000L * 7
    var dayInRow: Int = 0
    var highestScore: Int = 0
    private val drinks: MutableMap<Int, Int> = mutableMapOf()
    private val drinksWithChronology: MutableList<Triple<Int, Int, Date>> = mutableListOf()
    val drinksAllTime: MutableList<Map<Int, Int>> = mutableListOf()
    var waterStat = mutableListOf<Int>(0)

    fun addLiquid(amount: Int) {
        waterStat[waterStat.lastIndex] += amount
    }

    fun getCurrentWater(): Int {
        return waterStat.last()
    }

    fun clearTodayDrinks() {
        drinks.clear()
        drinksWithChronology.clear()
    }

    fun addDrink(id: Int, amount: Int) {
        drinks[id] = drinks[id]?.plus(amount) ?: amount
        drinksWithChronology.add(Triple(id, amount, Date()))
    }

    fun addNewDay() {
        drinksAllTime.add(drinks.toMutableMap())
        clearTodayDrinks()
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

    fun getDailyDrinkInfo(): MutableList<Triple<Int, Int, Date>> {
        return drinksWithChronology
    }

    fun getLastWeekStat(): List<Int> {
        val stat = mutableListOf<Int>()
        for (i in 0 until 7) {
            stat.add(waterStat[waterStat.lastIndex - i])
        }
        return stat
    }

    fun resetLastDay() {
        drinks.clear()
        drinksWithChronology.clear()
        waterStat[waterStat.lastIndex] = 0
    }

    fun checkMonthUnusage(drinkId: Int): Int {
        return if (drinksAllTime.size < 31) {
            0
        } else {
            for (i in 0 until 31) {
                if (drinksAllTime[drinksAllTime.lastIndex - i].containsKey(drinkId)){
                    return 0
                }
            }
            1
        }
    }
}