package com.example.hydrateme.waterfall

import java.io.Serializable

data class Achievment(val id: Byte, val name: String, val description: String, val exp: Int, val isSecret: Boolean, private val reward: Short, private val checkCompletion: (Int) -> Boolean) {
    fun isUpdated(value: Int): Pair<Boolean, Short> {
        return if (checkCompletion(value)) {
            Pair(true, reward)
        } else {
            Pair(false, reward)
        }
    }
}