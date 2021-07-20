package com.example.brainlessarmy.armylib

import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

class Miner(rotation: Int, resourceAmount: Int, hp: Int, unitType: MovingUnit) : Unit(rotation, resourceAmount, hp, unitType) {
    override fun makeStep(): Pair<Int, Int> {
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
            if (recourseAmount == 0) {
                turnTo(sourceX, sourceY)
            }
        }
        if (anotherHomeDistance < homeDistance) {
            homeDistance = anotherHomeDistance
            if (recourseAmount != 0) {
                turnTo(sourceX, sourceY)
            }
        }
        if (dangerDistance > anotherDangerDistance) {
            dangerDistance = anotherDangerDistance
            if (!isDirectionSafe()) {
                turnFrom(sourceX, sourceY)
            }
        }
    }

    override fun giveResources(sourceX: Int, sourceY: Int, anotherUnit: Unit) {
        homeDistance = 0
        hp = unitType.maxHP
        if (recourseAmount != 0) {
            if (anotherUnit.getResources(recourseAmount, sourceX, sourceY)) {
                recourseAmount = 0
            }
            turnFrom(sourceX, sourceY)
        }
    }

    override fun getResources(amount: Int, sourceX: Int, sourceY: Int): Boolean {
        goalDistance = 0
        if (recourseAmount == 0) {
            recourseAmount = amount
            turnFrom(sourceX, sourceY)
            return true
        }
        return false
    }
}