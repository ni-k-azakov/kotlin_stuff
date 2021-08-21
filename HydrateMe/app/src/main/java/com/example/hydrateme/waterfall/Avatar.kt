package com.example.hydrateme.waterfall

import java.io.Serializable

class Avatar(val id: Int,
             val nameResource: Int,
             val resourceId: Int,
             val resourceIdHappy: Int,
             val resourceIdSad: Int,
             val conditionDescriptionResource: Int,
             val conditionType: ConditionType,
             val conditionValue: Int) : Serializable