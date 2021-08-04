package com.example.hydrateme

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.hydrateme.waterfall.*
import com.hadiidbouk.charts.BarData
import com.hadiidbouk.charts.ChartProgressBar
import me.itangqi.waveloadingview.WaveLoadingView
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


class MainActivity : AppCompatActivity() {
    private lateinit var waterInfo: WaterInfo
    private lateinit var waterLoadingView: WaveLoadingView
    private lateinit var toastedList: ToastedList
    private val achievementList: MutableList<Achievement> = mutableListOf()
    private lateinit var profile: Profile
    private val drinkList: MutableList<Drink> = mutableListOf()
    private val dataList: MutableList<BarData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataLoader()
        fillAchievementList()
        fillAchievments()

        fillDrinksList()
        if (waterInfo.dayPassed(getFormula(profile.sex)(profile.weight, profile.actTime)) && waterInfo.getDayInRow() != 0) {
            profile.currentExp += 50
        }
        dataSaver()
    }

    override fun onStop() {
        dataSaver()
        super.onStop()
    }

    override fun onResume() {
        dataLoader()
        if (waterInfo.dayPassed(getFormula(profile.sex)(profile.weight, profile.actTime)) && waterInfo.getDayInRow() != 0) {
            profile.currentExp += 50
        }
        lvlCheck()
        updateViews()
        updateInfo()
        clearDrinksInfo()
        fillDrinksInfo()
        updateAchievments()
        updateGraph()
        updateAvatar()
        super.onResume()
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

        toastedList = if (File(this.filesDir.absolutePath + "/toasted_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/toasted_info_debug.dat"))
            inputStream.readObject() as ToastedList
        } else {
            ToastedList()
        }
    }

    fun dataSaver() {
        var outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/water_info_debug.dat"))
        outputStream.writeObject(waterInfo.storage)

        outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
        outputStream.writeObject(profile)

        outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/toasted_info_debug.dat"))
        outputStream.writeObject(toastedList)
    }

    private fun fillAchievementList() {
        var achievement = Achievement(
            0, R.string.ach_day_1,
                R.string.ach_day_1_des,
            50,
            false,
            1,
                R.drawable.achievement_1_day_n,
                R.drawable.achievement_1_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 1 }
        achievementList.add(achievement)
        achievement = Achievement(
            1, R.string.ach_day_3,
                R.string.ach_day_3_des,
            150,
            false,
            2,
                R.drawable.achievement_3_day_n,
                R.drawable.achievement_3_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 3 }
        achievementList.add(achievement)
        achievement = Achievement(
            2, R.string.ach_day_10,
                R.string.ach_day_10_des,
            200,
            false,
            3,
                R.drawable.achievement_10_day_n,
                R.drawable.achievement_10_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 10 }
        achievementList.add(achievement)
        achievement = Achievement(
            3, R.string.ach_day_40,
                R.string.ach_day_40_des,
            300, false,
            5,
                R.drawable.achievement_40_day_n,
                R.drawable.achievement_40_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 40 }
        achievementList.add(achievement)
        achievement = Achievement(
            4, R.string.ach_day_100,
                R.string.ach_day_100_des,
            1000,
            false,
            10,
                R.drawable.achievement_100_day_n,
                R.drawable.achievement_100_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 100 }
        achievementList.add(achievement)
        achievement = Achievement(
            5, R.string.ach_day_365,
                R.string.ach_day_365_des,
            2500,
            false,
            30,
                R.drawable.achievement_365_day_n,
                R.drawable.achievement_365_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 365 }
        achievementList.add(achievement)
        achievement = Achievement(
            6, R.string.ach_all,
                R.string.ach_all_des,
            500,
            true,
            10,
                R.drawable.achievement_hidden,
                R.drawable.achievement_all_drinks
        ) { level: Int -> level >= 8 }
        achievementList.add(achievement)
        achievement = Achievement(
            7, R.string.ach_lvl_3,
                R.string.ach_lvl_3_des,
            0,
            true,
            1,
                R.drawable.achievement_hidden,
                R.drawable.achievement_3_lvl
        ) { level: Int -> level >= 3 }
        achievementList.add(achievement)
        achievement = Achievement(
            8, R.string.ach_lvl_10,
                R.string.ach_lvl_10_des,
            0,
            true,
            2,
                R.drawable.achievement_hidden,
                R.drawable.achievement_10_lvl
        ) { level: Int -> level >= 10 }
        achievementList.add(achievement)
        achievement = Achievement(
            9, R.string.ach_lvl_20,
                R.string.ach_lvl_20_des,
            0,
            true,
            5,
                R.drawable.achievement_hidden,
                R.drawable.achievement_20_lvl
        ) { level: Int -> level >= 20 }
        achievementList.add(achievement)
        achievement = Achievement(
            10, R.string.ach_lvl_30,
                R.string.ach_lvl_30_des,
            0,
            true,
            10,
                R.drawable.achievement_hidden,
                R.drawable.achievement_30_lvl
        ) { level: Int -> level >= 30 }
        achievementList.add(achievement)
        achievement = Achievement(
            11, R.string.ach_lvl_50,
                R.string.ach_lvl_50_des,
            0,
            true,
            30,
                R.drawable.achievement_hidden,
                R.drawable.achievement_50_lvl
        ) { level: Int -> level >= 50 }
        achievementList.add(achievement)
        achievement = Achievement(
            12, R.string.ach_alco,
                R.string.ach_alco_des,
            250,
            true,
            10,
                R.drawable.achievement_hidden,
                R.drawable.achievement_alco
        ) { dayInRow: Int -> dayInRow >= 30 }
        achievementList.add(achievement)
        achievement = Achievement(
            13, R.string.ach_coffee,
                R.string.ach_coffee_des,
            250,
            true,
            10,
                R.drawable.achievement_hidden,
                R.drawable.achievement_coffee
        ) { dayInRow: Int -> dayInRow >= 30 }
        achievementList.add(achievement)
    }

    fun fillDrinksList() {
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

    fun updateAchievments() {
        var updated = false
        for (achievement in achievementList) {
            var completed = false
            for (completedAchievementId in profile.completedAchievmentsIdList) {
                if (completedAchievementId == achievement.id) {
                    completed = true
                }
            }
            if (completed) {
                continue
            }
            when (achievement.id) {
                in 0 until 6 -> {
                    val achState = achievement.isUpdated(waterInfo.getDayInRow())
                    if (achState.first) {
                        profile.completedAchievmentsIdList.add(achievement.id)
                        profile.currentExp += achievement.exp
                        profile.money += achievement.reward
                        var toasted = false
                        for (id in toastedList.toastedAchievments) {
                            if (id == achievement.id) {
                                toasted = true
                            }
                        }
                        if (!toasted) {
                            Toast.makeText(
                                this,
                                getText(achievement.nameResource).toString() + "\n" + getText(achievement.descriptionResource).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
                in 6 until 12 -> {
                    val achState = achievement.isUpdated(profile.lvl)
                    if (achState.first) {
                        profile.completedAchievmentsIdList.add(achievement.id)
                        profile.currentExp += achievement.exp
                        profile.money += achievement.reward
                        var toasted = false
                        for (id in toastedList.toastedAchievments) {
                            if (id == achievement.id) {
                                toasted = true
                            }
                        }
                        if (!toasted) {
                            Toast.makeText(
                                this,
                                getText(achievement.nameResource).toString() + "\n" + getText(achievement.descriptionResource).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
                in 12 until 14 -> {
                    val achState = achievement.isUpdated(waterInfo.getDayInRow())
                    if (achState.first) {
                        profile.completedAchievmentsIdList.add(achievement.id)
                        profile.currentExp += achievement.exp
                        profile.money += achievement.reward
                        var toasted = false
                        for (id in toastedList.toastedAchievments) {
                            if (id == achievement.id) {
                                toasted = true
                            }
                        }
                        if (!toasted) {
                            Toast.makeText(
                                this,
                                getText(achievement.nameResource).toString() + "\n" + getText(achievement.descriptionResource).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
            }
        }
        if (updated) {
            lvlCheck()
            clearAchievements()
            fillAchievments()
            findViewById<TextView>(R.id.achievementAmountView).text = getString(
                R.string.trophy_amount,
                profile.completedAchievmentsIdList.size
            )
            updateAchievments()
        }
    }

    private fun lvlCheck() {
        val expToLvlUp = { x: Int -> x * 50}
        while (expToLvlUp(profile.lvl) <= profile.currentExp) {
            profile.currentExp -= expToLvlUp(profile.lvl)
            profile.lvl += 1
        }
        findViewById<TextView>(R.id.lvlView).text = getString(R.string.lvl_info, profile.lvl)
        findViewById<TextView>(R.id.progressTextInfo).text = getString(
            R.string.progress_text, profile.currentExp, expToLvlUp(
                profile.lvl
            )
        )
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = expToLvlUp(profile.lvl)
        progressBar.progress = profile.currentExp
    }

    private fun fillAchievments() {
        var i = 0
        for (achievement in achievementList) {
            val layout = LinearLayout(this)
            layout.setBackgroundResource(achievement.resourceUndone)
            val text = TextView(this)
            text.text = if (achievement.isSecret) "?" else getText(achievement.nameResource)
            text.minLines = 2
            text.maxLines = 2
            text.ellipsize = TextUtils.TruncateAt.END
            text.gravity = Gravity.CENTER
            for (id in profile.completedAchievmentsIdList) {
                if (id == achievement.id) {
                    layout.setBackgroundResource(achievement.resourceDone)
                    layout.setOnClickListener {
                        Toast.makeText(this, getText(achievement.descriptionResource), Toast.LENGTH_SHORT).show()
                    }
                    text.text = getText(achievement.nameResource)
                }
            }

            when (i % 3) {
                0 -> {
                    findViewById<LinearLayout>(R.id.achiv1).addView(layout)
                    findViewById<LinearLayout>(R.id.achiv1).addView(text)
                }
                1 -> {
                    findViewById<LinearLayout>(R.id.achiv2).addView(layout)
                    findViewById<LinearLayout>(R.id.achiv2).addView(text)
                }
                2 -> {
                    findViewById<LinearLayout>(R.id.achiv3).addView(layout)
                    findViewById<LinearLayout>(R.id.achiv3).addView(text)
                }
            }
            val mParams: ViewGroup.LayoutParams = layout.layoutParams as ViewGroup.LayoutParams
            mParams.height = 60.dpToPixels(this).toInt()
            mParams.width = 60.dpToPixels(this).toInt()
            layout.layoutParams = mParams
            layout.postInvalidate()

            val tParams: LinearLayout.LayoutParams = text.layoutParams as LinearLayout.LayoutParams
            tParams.setMargins(0, 0, 0, 10.dpToPixels(this).toInt())
            text.layoutParams = tParams
            text.postInvalidate()

            i += 1
        }
    }

    fun clearAchievements() {
        findViewById<LinearLayout>(R.id.achiv1).removeAllViews()
        findViewById<LinearLayout>(R.id.achiv2).removeAllViews()
        findViewById<LinearLayout>(R.id.achiv3).removeAllViews()
    }

    fun fillDrinksInfo() {
        var i = 0
        for (drink in drinkList) {
            val cardLayout = CardView(this)
            cardLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardLayout.radius = 5.dpToPixels(this)
            cardLayout.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_blue))
            cardLayout.elevation = 0F

            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL

            val verticalLayout1 = LinearLayout(this)
            var vparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            verticalLayout1.layoutParams = vparams
            verticalLayout1.orientation = LinearLayout.VERTICAL
            verticalLayout1.gravity = Gravity.CENTER_VERTICAL

            val verticalLayout2 = LinearLayout(this)
            vparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            verticalLayout2.layoutParams = vparams
            verticalLayout2.orientation = LinearLayout.VERTICAL

            val iconLayout = LinearLayout(this)
            iconLayout.setBackgroundResource(drink.resourceId)

            val text = TextView(this)
            text.setText(drink.name)
            text.maxLines = 1
            text.isSingleLine = true
            text.ellipsize = TextUtils.TruncateAt.END

            val text2 = TextView(this)
            text2.text = getString(
                R.string.drink_amount,
                waterInfo.getDrinkAmount(drink.id) / 1000.0
            )
            text2.maxLines = 1
            text2.isSingleLine = true
            text2.setTextColor(ContextCompat.getColor(this, R.color.blue_number))
            text2.textSize = 24F
            text2.setTypeface(null, Typeface.BOLD)

            verticalLayout1.addView(iconLayout)

            verticalLayout2.addView(text2)
            verticalLayout2.addView(text)

            horizontalLayout.addView(verticalLayout1)
            horizontalLayout.addView(verticalLayout2)

            cardLayout.addView(horizontalLayout)

            when (i % 2) {
                0 -> {
                    findViewById<LinearLayout>(R.id.todayDrinks1).addView(cardLayout)
                }
                1 -> {
                    findViewById<LinearLayout>(R.id.todayDrinks2).addView(cardLayout)
                }
            }
            val mParams: ViewGroup.LayoutParams = iconLayout.layoutParams as ViewGroup.LayoutParams
            mParams.height = 35.dpToPixels(this).toInt()
            mParams.width = 35.dpToPixels(this).toInt()
            iconLayout.layoutParams = mParams
            iconLayout.postInvalidate()

            val cardParams: ViewGroup.MarginLayoutParams = cardLayout.layoutParams as ViewGroup.MarginLayoutParams
            cardParams.setMargins(
                5.dpToPixels(this).toInt(), 5.dpToPixels(this).toInt(), 5.dpToPixels(
                    this
                ).toInt(), 5.dpToPixels(this).toInt()
            )
            cardLayout.layoutParams = cardParams
            cardLayout.postInvalidate()

            val listViewParams: ViewGroup.MarginLayoutParams = verticalLayout1.layoutParams as ViewGroup.MarginLayoutParams
            listViewParams.setMargins(
                10.dpToPixels(this).toInt(), 5.dpToPixels(this).toInt(), 10.dpToPixels(
                    this
                ).toInt(), 5.dpToPixels(this).toInt()
            )
            verticalLayout1.layoutParams = listViewParams
            verticalLayout2.layoutParams = listViewParams
            verticalLayout1.postInvalidate()
            verticalLayout2.postInvalidate()
            i += 1
        }
    }

    fun clearDrinksInfo() {
        findViewById<LinearLayout>(R.id.todayDrinks1).removeAllViews()
        findViewById<LinearLayout>(R.id.todayDrinks2).removeAllViews()
    }

    fun openWaterStat(view: View) {
        dataSaver()
        startActivity(Intent(this, WaterActivity::class.java))
    }

    fun updateAvatar() {
        val avatarLayout = findViewById<ConstraintLayout>(R.id.avatar)
        avatarLayout.setBackgroundResource(profile.avatar.resourceId)
    }

    fun updateInfo() {
        findViewById<TextView>(R.id.waterInfoView).text = getString(
            R.string.water_info, waterInfo.getCurrentWater().toFloat() / 1000, getFormula(
                profile.sex
            )(profile.weight, profile.actTime)
        )
        val percent: Int = if (getFormula(profile.sex)(profile.weight, profile.actTime) == 0.0F) {
            0
        } else {
            floor(
                (waterInfo.getCurrentWater().toDouble() / 1000 / getFormula(profile.sex)(
                    profile.weight,
                    profile.actTime
                )) * 100
            ).toInt()
        }
        findViewById<TextView>(R.id.waterPercentage).text = getString(R.string.percentage, percent)
        findViewById<TextView>(R.id.moneyField).text = getString(R.string.int_number, profile.money)
        waterLoadingView = findViewById(R.id.waveLoaderView)
        waterLoadingView.progressValue = percent
        waterLoadingView.bottomTitle = String.format("%d%%", percent)
        waterLoadingView.centerTitle = ""
        waterLoadingView.topTitle = ""
    }

    fun updateGraph() {
        dataList.clear()
        val millis = System.currentTimeMillis()
        val formatter = SimpleDateFormat("dd/MM")
        var i = 1
        for (waterAmountStored in waterInfo.getLastWeekStat().reversed()) {
            var waterAmount = waterAmountStored
            if (waterAmount / 1000f > getFormula(profile.sex)(profile.weight, profile.actTime).toFloat()) {
                waterAmount = (getFormula(profile.sex)(profile.weight, profile.actTime).toFloat() * 1000).toInt()
            } else if (waterAmount < 0) {
                waterAmount = 0
            }
            val date = Date(millis - 86400000L * (7 - i))
            val data = BarData(
                formatter.format(date), waterAmount / 1000f, getString(
                    R.string.drink_amount,
                    waterAmountStored / 1000f
                )
            )
            dataList.add(data)
            i += 1
        }
        val mChart = findViewById<View>(R.id.ChartProgressBar) as ChartProgressBar
        mChart.setDataList(dataList as ArrayList<BarData>?)
        mChart.setMaxValue(getFormula(profile.sex)(profile.weight, profile.actTime).toFloat())
        mChart.build()
    }

    fun openSettings(view: View) {
        dataSaver()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun updateViews() {
        findViewById<TextView>(R.id.daysInRowTextView).text = waterInfo.getDayInRow().toString()
        findViewById<TextView>(R.id.achievementAmountView).text = getString(
                R.string.trophy_amount,
                profile.completedAchievmentsIdList.size
        )
        findViewById<TextView>(R.id.highestScoreView).text = getString(
                R.string.highest_score,
                waterInfo.getHighestScore()
        )
        findViewById<TextView>(R.id.lvlView).text = getString(R.string.lvl_info, profile.lvl)
        findViewById<TextView>(R.id.personName).text = profile.name
    }
}