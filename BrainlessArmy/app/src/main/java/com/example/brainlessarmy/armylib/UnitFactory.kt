package com.example.brainlessarmy.armylib

object UnitFactory {
    private val unitTypes: MutableMap<String, MovingUnit> = mutableMapOf()
    fun getUnitType(typeName: String): MovingUnit {
        return if (unitTypes.containsKey(typeName)) {
            unitTypes[typeName]!!
        } else {
            throw UnknownTypeException("$typeName type does not exist")
        }
    }
    fun createUnitType(name: String, color: Int, voiceLoudness: Int, speedLimitMin: Int, speedLimitMax: Int, maxHP: Int, damage: Int, type: UnitType): Boolean {
        return if (unitTypes.containsKey(name)) {
            false
        } else {
            unitTypes[name] = MovingUnit(color, voiceLoudness, speedLimitMin, speedLimitMax, maxHP, damage, type)
            true
        }
    }
}