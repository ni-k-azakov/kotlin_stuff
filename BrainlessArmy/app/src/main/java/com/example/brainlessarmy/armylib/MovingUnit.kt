package com.example.brainlessarmy.armylib

import kotlin.random.Random

class MovingUnit(val color: Int, val voiceLoudness: Int, private val speedLimitMin: Int, private val speedLimitMax: Int, val maxHP: Int, val damage: Int, val type: UnitType) {
    fun getSpeed(): Int {
        return Random.nextInt(speedLimitMin, speedLimitMax)
    }
}