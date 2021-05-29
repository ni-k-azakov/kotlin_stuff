package com.example.hydrateme.waterfall

import java.io.Serializable

class DataStorage : Serializable {
    private val serialVersionUID = 1L

    val drinks: MutableMap<Int, Int> = mutableMapOf()
    val drinksAllTime: MutableMap<Int, Int> = mutableMapOf()
    var waterStat = mutableListOf<Pair<Long, Short>>()

    fun addWater(amount: Short) {
        val currentTime = System.currentTimeMillis()
        if (waterStat.last().first % 86400000L < currentTime % 86400000L) {
            waterStat.add(Pair(currentTime, amount))
        } else {
            waterStat[waterStat.lastIndex] = Pair(waterStat.last().first, (waterStat.last().second + amount).toShort())
        }
    }

    fun getCurrentWater(): Short {
        val currentTime = System.currentTimeMillis()
        return if (waterStat.last().first % 86400000L == currentTime % 86400000L) {
            waterStat.last().second
        } else {
            0
        }
    }

    fun clear() {
        drinks.clear()
    }

    fun addDrink(id: Int, amount: Int) {
        if (drinks.containsKey(id)) {
            drinks[id] = drinks[id]!!.plus(amount)
        } else {
            drinks[id] = amount
        }
    }
}