package com.example.glassgo.waterfall

data class Achievement(val id: Byte, val nameResource: Int, val descriptionResource: Int, val exp: Int, val isSecret: Boolean, val reward: Short, val resourceUndone: Int, val resourceDone: Int, private val checkCompletion: (Int) -> Boolean) {
    fun isUpdated(value: Int): Pair<Boolean, Short> {
        return if (checkCompletion(value)) {
            Pair(true, reward)
        } else {
            Pair(false, reward)
        }
    }
}