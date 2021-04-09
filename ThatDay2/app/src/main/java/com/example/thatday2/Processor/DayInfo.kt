package com.example.thatday2.Processor

import com.example.thatday2.Processor.utils.DayInfoField

class DayInfo(var date: Long) {
    val mood: MutableList<Int> = mutableListOf()
    var bloodAmount: Int = 0
    var liquidFromPussy: Int = 0

    fun modifyDayInfo(field: DayInfoField, value: Int) {
        if (field == DayInfoField.MOOD) {
            if (!mood.contains(value)) {
                mood.add(value)
            } else if (field == DayInfoField.BLOOD) {
                bloodAmount = value
            } else if (field == DayInfoField.LIQUID) {
                liquidFromPussy = value
            }
        }
    }
}