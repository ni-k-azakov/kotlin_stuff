package com.example.brainlessarmy.armylib

import java.util.*
import kotlin.math.atan2
import kotlin.random.Random

class Field(width: Int, height: Int) {
    var units = Array(height) { Array<MutableList<Unit>>(width) { mutableListOf() } }

    fun createUnit(x: Int, y: Int, startRotation: Int, recourseAmount: Int, unitType: String) {
        val type = UnitFactory.getUnitType(unitType)
        when (type.type) {
            UnitType.SOURCE -> units[y][x].add(FoodSource(startRotation, recourseAmount, type.maxHP, type))
            UnitType.STORAGE -> units[y][x].add(Motherbase(startRotation, recourseAmount, type.maxHP, type))
            UnitType.MINION -> units[y][x].add(Miner(startRotation, recourseAmount, type.maxHP, type))
            UnitType.PATHFINDER -> units[y][x].add(Pathfinder(startRotation, recourseAmount, type.maxHP, type))
            UnitType.ENEMY -> units[y][x].add(DangerZone(startRotation, recourseAmount, type.maxHP, type))
            UnitType.ARMY -> units[y][x].add(Warrior(startRotation, recourseAmount, type.maxHP, type))
        }
    }
    fun makeStep() {
        val newUnitsField = Array(units.size) { Array<MutableList<Unit>>(units[units.lastIndex].size) { mutableListOf() } }
        for (i in units.indices) {
            for (j in units[i].indices) {
                units[i][j].sortBy { it.unitType.type.ordinal }
                for (unit in units[i][j]) {
                    val unitInfo = unit.scream()
                    for (tempY in -unitInfo[0] / 2..unitInfo[0] / 2) {
                        if (tempY + i >= units.size) {
                            break
                        }
                        if (tempY + i < 0) {
                            continue
                        }
                        for (tempX in -unitInfo[0] / 2..unitInfo[0] / 2) {
                            if (tempX + j >= units[i].size) {
                                break
                            }
                            if (tempX + j < 0) {
                                continue
                            }
                            when (unit.unitType.type) {
                                UnitType.SOURCE -> {
                                    for (reciever in units[i + tempY][j + tempX]) {
                                        unit.giveResources(tempX, tempY, reciever)
                                    }
                                }
                                UnitType.STORAGE -> {
                                    for (giver in units[i + tempY][j + tempX]) {
                                        if (giver.unitType.type != UnitType.SOURCE) {
                                            giver.giveResources(tempX, tempY, unit)
                                        }
                                    }
                                }
                                UnitType.MINION, UnitType.PATHFINDER, UnitType.ARMY -> {
                                    for (listener in units[i + tempY][j + tempX]) {
                                        listener.listen(unitInfo[1] + (unitInfo[0] / 2), unitInfo[2] + (unitInfo[0] / 2), unitInfo[3] + (unitInfo[0] / 2), tempX, tempY)
                                    }
                                }
                                UnitType.ENEMY -> {
                                    for (friend in units[i + tempY][j + tempX]) {
                                        if (friend.unitType.type != UnitType.ENEMY) {
                                            unit.dealDamage(friend)
                                            friend.dealDamage(unit)
                                        }
                                    }
                                }
                                else -> continue
                            }
                        }
                    }
                }
            }
        }
        for (i in units.indices) {
            for (j in units[i].indices) {
                for (unit in units[i][j]) {
                    if (unit.isAlive()) {
                        val newPosition = unit.makeStep()
                        if (i + newPosition.second >= units.size || i + newPosition.second < 0) {
                            unit.pongVertical()
                            newUnitsField[i][j].add(unit)
                        } else if (j + newPosition.first >= units[i].size || j + newPosition.first < 0) {
                            unit.pongHorizontal()
                            newUnitsField[i][j].add(unit)
                        } else {
                            newUnitsField[i + newPosition.second][j + newPosition.first].add(unit)
                        }
                    }
                }
            }
        }
        units = newUnitsField
    }

}