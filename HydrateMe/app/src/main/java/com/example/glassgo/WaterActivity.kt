package com.example.glassgo

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.glassgo.waterfall.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import me.itangqi.waveloadingview.WaveLoadingView
import java.io.*
import kotlin.math.floor

class WaterActivity : AppCompatActivity() {
    private val drinkList: MutableList<Drink> = mutableListOf()
    private lateinit var waterInfo: WaterInfo
    private lateinit var profile: Profile
    private lateinit var waterLoadingView: WaveLoadingView
    private var currentDrinkId = 0
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water)

        dataLoader()

        MobileAds.initialize(this)
        mAdView = findViewById(R.id.adView)
        val adRequest: AdRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        fillDrinksList()
        showDrinksList()
        updateInfo()

        findViewById<TextView>(R.id.daysInRow).text = getString(R.string.days_in_row, waterInfo.getDayInRow())
    }

    override fun onPause() {
        dataSaver()
        mAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()
    }

    private fun fillDrinksList() {
        var drink = Drink(0, R.string.drink_water, R.drawable.water, 1.0F)
        drinkList.add(drink)
        drink = Drink(1, R.string.drink_water_gas, R.drawable.water_gas, 0.8F)
        drinkList.add(drink)
        drink = Drink(2, R.string.drink_tea, R.drawable.tea, 0.85F)
        drinkList.add(drink)
        drink = Drink(3, R.string.drink_coffee, R.drawable.coffee, 0.6F)
        drinkList.add(drink)
        drink = Drink(4, R.string.drink_coffee_milk, R.drawable.coffee_milk, 0.2F)
        drinkList.add(drink)
        drink = Drink(5, R.string.drink_alco, R.drawable.alco, -1.6F)
        drinkList.add(drink)
        drink = Drink(6, R.string.drink_energy, R.drawable.energy, -0.8F)
        drinkList.add(drink)
    }

    private fun showDrinksList() {
        findViewById<LinearLayout>(R.id.drink_list).removeAllViews()
        for (drink in drinkList) {
            val verticalLayout1 = LinearLayout(this)
            var vparams = LinearLayout.LayoutParams(
                    70.dpToPixels(this).toInt(),
                    70.dpToPixels(this).toInt()
            )
            verticalLayout1.layoutParams = vparams
            verticalLayout1.orientation = LinearLayout.VERTICAL

            val layout = LinearLayout(this)
            layout.setBackgroundResource(drink.resourceId)
            val checkLayout = LinearLayout(this)
            if (drink.id == currentDrinkId) {
                checkLayout.setBackgroundResource(R.drawable.dot)
            } else {
                checkLayout.setBackgroundResource(R.drawable.dot_inactive)
            }
            layout.contentDescription = drink.id.toString()
            layout.setOnClickListener {
                currentDrinkId = it.contentDescription.toString().toInt()
                showDrinksList()
            }
            verticalLayout1.addView(layout)
            verticalLayout1.addView(checkLayout)
            findViewById<LinearLayout>(R.id.drink_list).addView(verticalLayout1)
            val mParams: LinearLayout.LayoutParams = layout.layoutParams as LinearLayout.LayoutParams
            mParams.width = 50.dpToPixels(this).toInt()
            mParams.height = 50.dpToPixels(this).toInt()
            mParams.setMargins(10.dpToPixels(this).toInt(), 0, 10.dpToPixels(this).toInt(), 0)
            layout.layoutParams = mParams
            val nParams: LinearLayout.LayoutParams = checkLayout.layoutParams as LinearLayout.LayoutParams
            nParams.width = 20.dpToPixels(this).toInt()
            nParams.height = 20.dpToPixels(this).toInt()
            nParams.setMargins(26.dpToPixels(this).toInt(), 0, 24.dpToPixels(this).toInt(), 0)
            checkLayout.layoutParams = nParams
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

    fun updateAvatar(percent: Int) {
        val avatarLayout = findViewById<ImageView>(R.id.avatar)
        when {
            percent >= 100 -> {
                avatarLayout.setImageResource(profile.avatar.resourceIdHappy)
            }
            percent in 50..99 -> {
                avatarLayout.setImageResource(profile.avatar.resourceId)
            }
            percent in 0..49 -> {
                avatarLayout.setImageResource(profile.avatar.resourceIdSad)
            }
            percent < 0 -> {
                avatarLayout.setImageResource(profile.avatar.resourceIdSuperSad)
            }
        }
        findViewById<ImageView>(R.id.avatarMaskWater).setImageResource(profile.mask.resourceId)
        findViewById<ImageView>(R.id.avatarHatWater).setImageResource(profile.hat.resourceId)
    }

    fun updateInfo() {
        findViewById<TextView>(R.id.waterIsFrom).text = if (waterInfo.getCurrentWater().toFloat() / 1000 > 0) {
            getString(
                    R.string.water_info, waterInfo.getCurrentWater().toFloat() / 1000, getFormula(
                    profile.sex
            )(profile.weight, profile.actTime)
            )
        } else {
            getString(
                    R.string.water_info, 0F, getFormula(
                    profile.sex
            )(profile.weight, profile.actTime)
            )
        }
        val percent: Int = if (getFormula(profile.sex)(profile.weight, profile.actTime) == 0.0F) {
            0
        } else {
            floor((waterInfo.getCurrentWater().toDouble() / 1000 / getFormula(profile.sex)(profile.weight, profile.actTime)) * 100).toInt()
        }
        findViewById<TextView>(R.id.waterPercent).text = getString(R.string.percentage, percent)
        updateAvatar(percent)
        waterLoadingView = findViewById(R.id.waveLoaderView)
        waterLoadingView.progressValue = percent
        waterLoadingView.bottomTitle = String.format("%d%%", percent)
        waterLoadingView.centerTitle = ""
        waterLoadingView.topTitle = ""
    }

    fun addLiquid(view: View) {
        val amount = (view.contentDescription.toString().toFloat() * 1000).toInt()
        waterInfo.addLiquid(currentDrinkId, drinkList[currentDrinkId].calc(amount), amount)
        updateInfo()
    }

    fun resetWater(view: View) {
        waterInfo.reset()
        updateInfo()
    }
}