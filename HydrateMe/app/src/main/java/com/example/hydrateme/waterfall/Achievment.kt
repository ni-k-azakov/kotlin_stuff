package com.example.hydrateme.waterfall

data class Achievment(val id: Byte, val name: String, val description: String, val exp: Int, val isSecret: Boolean, val reward: Short, val resourceUndone: Int, val resourceDone: Int,  private val checkCompletion: (Int) -> Boolean) {
    fun isUpdated(value: Int): Pair<Boolean, Short> {
        return if (checkCompletion(value)) {
            Pair(true, reward)
        } else {
            Pair(false, reward)
        }
    }
}