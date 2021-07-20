package com.example.brainlessarmy.armylib

import kotlin.random.Random


class FoodSource(rotation: Int, resourceAmount: Int, hp: Int, unitType: MovingUnit) : Unit(rotation, resourceAmount, hp, unitType) {
    private var toGive = Random.nextInt(3, 6) % (recourseAmount + 1)
    override fun giveResources(sourceX: Int, sourceY: Int, anotherUnit: Unit) {
        var currentToGive = toGive
        if (recourseAmount == 0) {
            currentToGive = 0
        }
        if (anotherUnit.getResources(currentToGive, sourceX, sourceY)) {
            recourseAmount -= currentToGive
            toGive = Random.nextInt(3, 6) % (recourseAmount + 1)
        }
    }
    override fun takeDamage(damageAmount: Int) {
        return
    }
    override fun isAlive(): Boolean {
        return !(recourseAmount == 0 || hp <= 0)
    }
}