package com.example.hydrateme.waterfall

fun getFormula(sex: Byte): (Float, Float) -> Double {
    when (sex) {
        0.toByte() -> return { weight: Float, actTime: Float -> weight * 0.03 + actTime * 0.5 }
        1.toByte() -> return { weight: Float, actTime: Float -> weight * 0.025 + actTime * 0.4 }
        else -> throw InvalidArgumentException("Sex argument can be 0 or 1. $sex found")
    }
}