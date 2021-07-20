package com.example.brainlessarmy.armylib

class Motherbase(rotation: Int, resourceAmount: Int, hp: Int, unitType: MovingUnit) : Unit(rotation, resourceAmount, hp, unitType) {
    override fun getResources(amount: Int, sourceX: Int, sourceY: Int): Boolean {
        recourseAmount += amount
        return true
    }
    override fun takeDamage(damageAmount: Int) {
        return
    }
}