package com.example.brainlessarmy

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.brainlessarmy.armylib.Field
import com.example.brainlessarmy.armylib.UnitFactory
import com.example.brainlessarmy.armylib.UnitType
import processing.android.PFragment
import processing.core.PApplet
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    val field = Field(100, 100)
    var iterAmount = 0
    val myCanvas = object : PApplet() {
        override fun settings() {
            loop()
            size(700, 700, P3D)
        }

        override fun draw() {
            var population = 0
            field.makeStep()
            iterAmount += 1
            camera(width / 2.0F, mouseY.toFloat(), (height / 2.0F) / tan(PI * 30.0F / 180.0F), width / 2F, height / 2F, 0F, 0F, 1F, 0F)
            background(Color.GREEN)
            fill(Color.BLACK)
            rect(100F, 100F, 500F, 500F)
            translate(100F, 100F, 1F)
            stroke(Color.BLACK) // обводка
            for (i in field.units.indices) {
                for (j in field.units[i].indices) {
                    for (unit in field.units[i][j]) {
                        population += 1
                        fill(unit.unitType.color) // заливка
                        if (unit.unitType.type == UnitType.MINION) {
                            if (unit.tellAllResourceAmount() != 0) {
                                fill(Color.YELLOW)
                            }
                            ellipse(j.toFloat() * 5F, i.toFloat() * 5F, 6f, 6f)
                        } else if (unit.unitType.type == UnitType.PATHFINDER || unit.unitType.type == UnitType.ARMY) {
                            if (unit.unitType.type == UnitType.ARMY) {
                                if (!unit.isDirectionSafe()) {
                                    fill(Color.RED)
                                }
                            }
                            ellipse(j.toFloat() * 5F, i.toFloat() * 5F, 6f, 6f)
                        } else {
                            ellipse(j.toFloat() * 5F, i.toFloat() * 5F, unit.unitType.voiceLoudness.toFloat() / 2 * 5F, unit.unitType.voiceLoudness.toFloat() / 2 * 5F)
                            textSize(20F)
                            textAlign(CENTER)
                            fill(Color.WHITE)
                            if (unit.unitType.type == UnitType.ENEMY) {
                                fill(Color.WHITE)
                                textAlign(LEFT)
                                text(unit.hp.toString(), j.toFloat() * 5F, i.toFloat() * 5F - 20)
                            } else {
                                text(unit.tellAllResourceAmount().toString(), j.toFloat() * 5F, i.toFloat() * 5F - 20)
                            }
                            if (unit.unitType.type == UnitType.STORAGE) {
                                fill(Color.WHITE)
                                textAlign(LEFT)
                                text("GPI: " + (unit.tellAllResourceAmount() / iterAmount.toFloat()).toString(), 50F, 50F)
                            }
                        }
                    }
                }
            }
            fill(Color.WHITE)
            textAlign(LEFT)
            text("POPULATION: $population", 50F, 100F)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UnitFactory.createUnitType("Рабочий", Color.CYAN, 20, 2, 5, 100, 0, UnitType.MINION)
        UnitFactory.createUnitType("Фабрика", Color.MAGENTA, 10, 1, 2, 100, 0, UnitType.SOURCE)
        UnitFactory.createUnitType("Склад", Color.GREEN, 10, 1, 2, 100, 0, UnitType.STORAGE)
        UnitFactory.createUnitType("Разведчик", Color.GRAY, 20, 4, 7, 100, 0, UnitType.PATHFINDER)
        UnitFactory.createUnitType("Замок дебилов", Color.RED, 10, 1, 2, 10000, 50, UnitType.ENEMY)
        UnitFactory.createUnitType("Воин", Color.GREEN, 10, 2, 4, 1000, 2, UnitType.ARMY)
        field.createUnit(90, 11, 50, 5000, "Фабрика")
        field.createUnit(90, 89, 130, 5000, "Фабрика")
        field.createUnit(11, 50, 45, 0, "Склад")
        field.createUnit(50, 30, 45, 0, "Замок дебилов")

        for (i in 0 until 200) {
            field.createUnit(50, 50, 2, 0, "Рабочий")
        }
//        for (i in 0 until 50) {
//            field.createUnit(50, 50, 2, 0, "Разведчик")
//        }
        for (i in 0 until 150) {
            field.createUnit(50, 50, 2, 0, "Воин")
        }
        // Помещаем холст в фрагмент
        val myFragment = PFragment(myCanvas)
        // Выводим фрагмент
        myFragment.setView(findViewById(R.id.canvasContainer), this)
    }
    fun addRecource(view: View) {
        field.createUnit(Random.nextInt(0, 100), Random.nextInt(0, 100), 130, 5000, "Фабрика")
    }
}