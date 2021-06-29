package com.example.hydrateme.waterfall

import java.io.Serializable

class Profile : Serializable {
    private val serialVersionUID = 2L
    var name = ""
    var currentExp = 0
    var lvl = 1
    var avatar: Int = -1
    var sex: Byte = 0
    var actTime: Float = 2.0F
    var weight: Float = 72.0F
    val completedAchievmentsIdList: MutableList<Byte> = mutableListOf()
}