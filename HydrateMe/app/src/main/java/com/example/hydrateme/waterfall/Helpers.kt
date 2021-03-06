package com.example.hydrateme.waterfall

import android.content.Context
import android.util.TypedValue
import java.io.Serializable

fun getFormula(sex: Byte): (Float, Float) -> Double {
    when (sex) {
        0.toByte() -> return { weight: Float, actTime: Float -> weight * 0.03 + actTime * 0.5 }
        1.toByte() -> return { weight: Float, actTime: Float -> weight * 0.025 + actTime * 0.4 }
        else -> throw InvalidArgumentException("Sex argument can be 0 or 1. $sex found")
    }
}

fun Int.dpToPixels(context: Context): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
)

class ToastedList : Serializable{
    private val serialVersionUID = 3L
    fun addAchievement(id: Byte) {
        toastedAchievments.add(id)
    }
    val toastedAchievments: MutableList<Byte> = mutableListOf()
}