package com.example.hydrateme.waterfall

import java.io.Serializable

class Avatar(val id: Int, val nameResource: Int, val resourceId: Int, val conditionDescriptionResource: Int, val conditionType: ConditionType, private val conditionValue: Int) : Serializable {
    fun isUpdated(value: Int): Boolean {
        return value >= conditionValue
    }
}