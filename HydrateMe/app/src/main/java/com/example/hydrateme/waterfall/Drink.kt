package com.example.hydrateme.waterfall

import kotlin.math.ceil

class Drink(val id: Int, val name: String, var resourceId: Int, private val coefficient: Float) {
    fun calc(value: Int): Int {
        return ceil(value * coefficient).toInt()
    }
}