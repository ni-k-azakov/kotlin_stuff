package com.example.hydrateme.waterfall

class Achievment(val id: Byte, val name: String, val description: String, val exp: Int, val isSecret: Boolean, private val reward: Short, private val checkCompletion: (Int) -> Boolean) {
    var done: Boolean = false

    fun isUpdated(value: Int): Pair<Boolean, Short> {
        return if (done) {
            Pair(false, reward)
        } else {
            if (checkCompletion(value)) {
                done = true
                Pair(done, reward)
            } else {
                Pair(false, reward)
            }
        }
    }
}