package com.example.hydrateme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hydrateme.waterfall.*
import com.example.hydrateme.waterfall.DataStorage
import com.example.hydrateme.waterfall.Drink
import com.example.hydrateme.waterfall.WaterInfo
import me.itangqi.waveloadingview.WaveLoadingView
import org.w3c.dom.Text
import java.io.*
import kotlin.math.floor

class WaterActivity : AppCompatActivity() {
    private val drinkList: MutableList<Drink> = mutableListOf()
    private lateinit var waterInfo: WaterInfo
    private lateinit var profile: Profile
    private lateinit var waterLoadingView: WaveLoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)

        dataLoader()

        fillDrinksList()
        showDrinksList()
        updateAvatar()
        updateInfo()

        findViewById<TextView>(R.id.daysInRow).text = getString(R.string.days_in_row, waterInfo.getDayInRow())
    }

    override fun onPause() {
        dataSaver()
        super.onPause()
    }

    private fun fillDrinksList() {
        var drink = Drink(0, "Вода", R.drawable.water, 1.0F)
        drinkList.add(drink)
        drink = Drink(1, "Вода с газом", R.drawable.water_gas, 0.8F)
        drinkList.add(drink)
        drink = Drink(2, "Чай", R.drawable.tea, 0.85F)
        drinkList.add(drink)
        drink = Drink(3, "Кофе", R.drawable.coffee, 0.6F)
        drinkList.add(drink)
        drink = Drink(4, "Кофе с молоком", R.drawable.coffee_milk, 0.2F)
        drinkList.add(drink)
        drink = Drink(5, "Алкоголь", R.drawable.alco, -1.6F)
        drinkList.add(drink)
        drink = Drink(6, "Энергетик", R.drawable.energy, -0.8F)
        drinkList.add(drink)
    }

    private fun showDrinksList() {
        for (drink in drinkList) {
            val layout = LinearLayout(this)
            layout.setBackgroundResource(drink.resourceId)
            layout.contentDescription = drink.id.toString()
            layout.setOnClickListener {
                waterInfo.addLiquid(it.contentDescription.toString().toInt(), drinkList[it.contentDescription.toString().toInt()].calc(500), 500)
                updateInfo()
            }
            findViewById<LinearLayout>(R.id.drink_list).addView(layout)
            val mParams: LinearLayout.LayoutParams = layout.layoutParams as LinearLayout.LayoutParams
            mParams.width = 50.dpToPixels(this).toInt()
            mParams.setMargins(20.dpToPixels(this).toInt(), 0, 20.dpToPixels(this).toInt(), 0)
            layout.layoutParams = mParams
            layout.postInvalidate()
        }

    }

    private fun dataLoader() {
        waterInfo = if (File(this.filesDir.absolutePath + "/water_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/water_info_debug.dat"))
            WaterInfo(inputStream.readObject() as DataStorage)
        } else {
            WaterInfo(DataStorage())
        }

        profile = if (File(this.filesDir.absolutePath + "/profile_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
            inputStream.readObject() as Profile
        } else {
            Profile()
        }
    }

    fun dataSaver() {
        var outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/water_info_debug.dat"))
        outputStream.writeObject(waterInfo.storage)

        outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
        outputStream.writeObject(profile)
    }

    fun updateAvatar() {
        val avatarLayout = findViewById<ConstraintLayout>(R.id.avatar)
        avatarLayout.setBackgroundResource(profile.avatar)
    }

    fun updateInfo() {
        findViewById<TextView>(R.id.waterIsFrom).text = getString(R.string.water_info, waterInfo.getCurrentWater().toFloat() / 1000, getFormula(profile.sex)(profile.weight, profile.actTime))
        val percent: Int = if (getFormula(profile.sex)(profile.weight, profile.actTime) == 0.0) {
            0
        } else {
            floor((waterInfo.getCurrentWater().toDouble() / 1000 / getFormula(profile.sex)(profile.weight, profile.actTime)) * 100).toInt()
        }
        findViewById<TextView>(R.id.waterPercent).text = getString(R.string.percentage, percent)

        waterLoadingView = findViewById(R.id.waveLoaderView)
        waterLoadingView.progressValue = percent
        waterLoadingView.bottomTitle = String.format("%d%%", percent)
        waterLoadingView.centerTitle = ""
        waterLoadingView.topTitle = ""
    }
}