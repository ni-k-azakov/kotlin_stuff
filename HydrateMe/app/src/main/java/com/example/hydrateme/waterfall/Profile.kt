package com.example.hydrateme.waterfall

import java.io.Serializable

class Profile : Serializable {
    private val serialVersionUID = 2L
    var name = "Введите имя"
    var currentExp = 0
    var lvl = 0
    var avatar: Byte = 0
    val completedAchievmentsList: MutableList<Achievment> = mutableListOf()
}