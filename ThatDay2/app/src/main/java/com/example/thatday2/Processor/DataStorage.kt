package com.example.thatday2.Processor

import java.io.Serializable
import java.util.*

class DataStorage : Serializable {
    private val serialVersionUID = 1L

    val periodDays: MutableList<Long> = mutableListOf()
    val periodsDurations: MutableList<Long> = mutableListOf()
    val cycleDurations: MutableList<Long> = mutableListOf()
    var firstPeriodDaySaved = 0L

    fun addOrRemoveDate(date: Long) {
        if (periodDays.contains(date)) {
            periodDays.remove(date)
        } else {
            periodDays.add(date)
        }
    }

    fun updateDurations() {
        periodsDurations.clear()
        cycleDurations.clear()
        periodDays.sort()
        if (periodDays.size == 0) return
        var firstPeriodDay: Long = periodDays[0]
        var lastPeriodDay: Long = periodDays[0]
        for (i in 0 until periodDays.size - 1) {
            if (periodDays[i + 1] - periodDays[i] > dayToMillis(3)) {
                if (firstPeriodDay != 0.toLong()) {
                    cycleDurations.add(periodDays[i + 1] - firstPeriodDay)
                    periodsDurations.add(lastPeriodDay - firstPeriodDay + dayToMillis(1))
                }
                firstPeriodDaySaved = firstPeriodDay
                firstPeriodDay = periodDays[i + 1]
                lastPeriodDay = periodDays[i + 1]
            } else {
                lastPeriodDay = periodDays[i + 1]
            }
            if (i + 1 == periodDays.lastIndex && Calendar.getInstance().timeInMillis - periodDays[i + 1] > dayToMillis(3)) {
                periodsDurations.add(lastPeriodDay - firstPeriodDay + dayToMillis(1))
                firstPeriodDaySaved = firstPeriodDay
            }
        }
    }

    fun getPeriodDurInfo(): MutableList<Long> {
        return periodsDurations
    }
    fun getCycleDurInfo(): MutableList<Long> {
        return cycleDurations
    }
    fun getLatestPeriodFirstDay(): Long {
        return firstPeriodDaySaved
    }
    private fun dayToMillis(dayAmount: Int): Long {
        return dayAmount.toLong() * 86400000
    }

}