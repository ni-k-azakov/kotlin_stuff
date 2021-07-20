package com.example.brainlessarmy.armylib

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.random.Random

open class Unit(var rotation: Int, var recourseAmount: Int, var hp: Int, var unitType: MovingUnit) {
    var goalDistance = 0
    var homeDistance = 0
    var dangerDistance = 0

    open fun makeStep(): Pair<Int, Int> {
        val x = round(cos(rotation * 0.0174533) * unitType.getSpeed()).toInt()
        val y = round(sin(rotation * 0.0174533) * unitType.getSpeed()).toInt()
        mutateRotation()
        return Pair(x, y)
    }
    open fun listen(anotherGoalDistance: Int, anotherHomeDistance: Int, anotherDangerDistance: Int, sourceX: Int, sourceY: Int) {}
    fun scream(): List<Int> {
        return listOf(unitType.voiceLoudness, goalDistance, homeDistance, dangerDistance)
    }
    open fun tellAllResourceAmount(): Int {
        return recourseAmount
    }
    open fun giveResources(sourceX: Int, sourceY: Int, anotherUnit: Unit) {}
    open fun getResources(amount: Int, sourceX: Int, sourceY: Int): Boolean {
        return false
    }
    protected open fun mutateRotation() {
        val newAngle = round(Random.nextFloat() * 2).toInt()
        if (Random.nextInt(0, 2) == 0) {
            rotation += newAngle
        } else {
            rotation -= newAngle
        }
    }
    open fun pongHorizontal() {
        rotation = (180 - rotation + 360) % 360
    }
    open fun pongVertical() {
        rotation = 360 - rotation
    }
    protected fun turnTo(x: Int, y: Int) {
        rotation = (Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toInt() + 540) % 360
    }
    protected fun turnFrom(x: Int, y: Int) {
        rotation = (Math.toDegrees(atan2(-y.toDouble(), -x.toDouble())).toInt() + 540) % 360
    }
    open fun isDirectionSafe(): Boolean {
        return dangerDistance > unitType.getSpeed() * 5 + unitType.voiceLoudness / 2
    }
    open fun takeDamage(damageAmount: Int) {
        hp -= damageAmount
        dangerDistance = 0
    }
    open fun dealDamage(anotherUnit: Unit) {
        anotherUnit.takeDamage(unitType.damage)
    }
    open fun isAlive(): Boolean {
        return hp > 0
    }
}