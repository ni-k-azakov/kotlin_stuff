package com.example.glassgo.waterfall

import android.content.Context
import android.util.TypedValue
import java.io.Serializable
import kotlin.math.floor

fun getFormula(sex: Byte): (Float, Float) -> Float {
    when (sex) {
        0.toByte() -> return { weight: Float, actTime: Float -> floor((weight * 0.03F + actTime * 0.5F) * 100F) / 100F}
        1.toByte() -> return { weight: Float, actTime: Float -> floor((weight * 0.025F + actTime * 0.4F) * 100F) / 100F }
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