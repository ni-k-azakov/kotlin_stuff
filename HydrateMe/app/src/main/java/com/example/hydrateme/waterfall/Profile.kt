package com.example.hydrateme.waterfall

import java.io.Serializable

class Profile : Serializable {
    private val serialVersionUID = 2L
    var name = ""
    var currentExp = 0
    var lvl = 1
    var avatar: Byte = 0
    var dayInRow: Int = 0
    var highestScore: Int = 0
    var sex: Byte = 0
    var actTime: Float = 0.0F
    var weight: Float = 0.0F
    val completedAchievmentsIdList: MutableList<Byte> = mutableListOf()
}