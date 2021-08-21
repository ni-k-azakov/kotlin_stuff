package com.example.hydrateme.waterfall

import com.example.hydrateme.R
import java.io.Serializable

class Profile : Serializable {
    private val serialVersionUID = 2L
    var name = ""
    var currentExp = 0
    var lvl = 1
    var avatar: Avatar = Avatar(
        0,
        R.string.avatar_blue_drop,
        R.drawable.drop,
        R.drawable.drop,
        R.drawable.drop,
        R.string.avatar_base_des,
        ConditionType.NONE,
        0
    )
    var hat: Clothes = Clothes(
        0,
        R.string.nothing,
        R.drawable.nothing,
        ConditionType.NONE,
        0
    )
    var mask: Clothes = Clothes(
        0,
        R.string.nothing,
        R.drawable.nothing,
        ConditionType.NONE,
        0
    )
    var sex: Byte = 0
    var actTime: Float = 2.0F
    var weight: Float = 72.0F
    var money: Int = 0
    var advertCoins: Int = 0
    var lastAdvertShow: Long = System.currentTimeMillis() - 86400000L * 7
    val completedAchievmentsIdList: MutableList<Byte> = mutableListOf()
    val availableAvatarIdList: MutableList<Byte> = mutableListOf(0, 1)
    val availableMaskIdList: MutableList<Byte> = mutableListOf(0)
    val availableHatIdList: MutableList<Byte> = mutableListOf(0)
}