package com.example.hydrateme.waterfall

import java.io.Serializable

class Avatar(val id: Int, val nameResource: Int, val resourceId: Int, val conditionDescriptionResource: Int, val conditionType: ConditionType, val conditionValue: Int) : Serializable