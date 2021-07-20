package com.example.brainlessarmy.armylib

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.random.Random

class Pathfinder(rotation: Int, resourceAmount: Int, hp: Int, unitType: MovingUnit) : Unit(rotation, resourceAmount, hp, unitType) {
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
        }
        if (anotherHomeDistance < homeDistance) {
            homeDistance = anotherHomeDistance
            if (!isDirectionSafe()) {
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
    }

    override fun getResources(amount: Int, sourceX: Int, sourceY: Int): Boolean {
        goalDistance = 0
        return false
    }
    override fun mutateRotation() {
        val newAngle = round(Random.nextFloat() * 5).toInt()
        if (Random.nextInt(0, 2) == 0) {
            rotation += newAngle
        } else {
            rotation -= newAngle
        }
    }
}