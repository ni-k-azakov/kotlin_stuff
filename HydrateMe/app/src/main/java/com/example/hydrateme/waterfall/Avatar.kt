package com.example.hydrateme.waterfall

import java.io.Serializable

class Avatar(val id: Int, val name: String, val resourceId: Int, val conditionDescription: String, val conditionType: ConditionType, private val conditionValue: Int) : Serializable {
    fun isUpdated(value: Int): Boolean {
        return value >= conditionValue
    }
}