package com.example.brainlessarmy.armylib

import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

class Warrior(rotation: Int, resourceAmount: Int, hp: Int, unitType: MovingUnit) : Unit(rotation, resourceAmount, hp, unitType) {
    private var isFighting = false
    override fun makeStep(): Pair<Int, Int> {
        if (isFighting) {
            goalDistance += unitType.getSpeed()
            homeDistance += unitType.getSpeed()
            isFighting = false
            return Pair(0, 0)
        }
        goalDistance += unitType.getSpeed()
        homeDistance += unitType.getSpeed()
        dangerDistance += unitType.getSpeed()
        val x = round(cos(rotation * 0.0174533) * unitType.getSpeed()).toInt()
        val y = round(sin(rotation * 0.0174533) * unitType.getSpeed()).toInt()
        mutateRotation()
        return Pair(x, y)
    }

    override fun listen(anotherGoalDistance: Int, anotherHomeDistance: Int, anotherDangerDistance: Int, sourceX: Int, sourceY: Int) {
        if (anotherGoalDistance < goalDistance) {
            goalDistance = anotherGoalDistance
        }
        if (dangerDistance > anotherDangerDistance) {
            dangerDistance = anotherDangerDistance
            if (isDirectionSafe()) {
                turnTo(sourceX, sourceY)
            } else {
                turnFrom(sourceX, sourceY)
            }
        }
        if (anotherHomeDistance < homeDistance) {
            homeDistance = anotherHomeDistance
            if (!isDirectionSafe()) {
                turnTo(sourceX, sourceY)
            }
        }
    }
    override fun giveResources(sourceX: Int, sourceY: Int, anotherUnit: Unit) {
        homeDistance = 0
        hp = unitType.maxHP
    }
    override fun getResources(amount: Int, sourceX: Int, sourceY: Int): Boolean {
        goalDistance = 0
        return false
    }
    override fun dealDamage(anotherUnit: Unit) {
        isFighting = isDirectionSafe()
        anotherUnit.takeDamage(unitType.damage)
    }
    override fun isDirectionSafe(): Boolean {
        return (hp / unitType.maxHP.toFloat()) > 0.35
    }
}