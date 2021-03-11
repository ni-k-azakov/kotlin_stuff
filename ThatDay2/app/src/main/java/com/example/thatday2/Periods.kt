package com.example.thatday2

class Periods {
    var averagePeriodsDuration: Int = 5
        get() {
            return 5
        }
        set(value) {
            field = value
        }
    var averageCycleDuration: Int = 28
    val savedData: DataStorage = DataStorage()
}