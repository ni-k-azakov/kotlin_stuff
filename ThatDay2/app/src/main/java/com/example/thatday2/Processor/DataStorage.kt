package com.example.thatday2.Processor

import com.example.thatday2.Processor.utils.Month
import java.util.*

class DataStorage {
    val periodDays: MutableList<Long> = mutableListOf()
    val periodsDurations: MutableList<Long> = mutableListOf()
    val cycleDurations: MutableList<Long> = mutableListOf()

    fun addDate(date: Long) {
        periodDays.add(date)
    }

    fun deleteDate(date: Long) {
        periodDays.remove(date)
    }

    fun updateDurations() {
        periodsDurations.clear()
        periodDays.sort()
        var firstPeriodDay: Long = periodDays[0]
        var lastPeriodDay: Long = periodDays[0]
        for (i in 0 until periodDays.size - 1) {
            if (periodDays[i + 1] - periodDays[i] > dayToMillis(3)) {
                if (firstPeriodDay != 0.toLong()) {
                    cycleDurations.add(periodDays[i + 1] - firstPeriodDay)
                    periodsDurations.add(lastPeriodDay - firstPeriodDay)
                }
                firstPeriodDay = periodDays[i + 1]
                lastPeriodDay = periodDays[i + 1]
            } else {
                lastPeriodDay = periodDays[i + 1]
            }
        }
    }

    fun getPeriodDurInfo(): MutableList<Long> {
        return periodsDurations
    }
    fun getCycleDurInfo(): MutableList<Long> {
        return cycleDurations
    }

    private fun dayToMillis(dayAmount: Int): Long {
        return dayAmount.toLong() * 86400000
    }
}