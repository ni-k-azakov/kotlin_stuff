package com.example.glassgo

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.glassgo.waterfall.*
import com.google.android.gms.ads.*
import com.hadiidbouk.charts.BarData
import com.hadiidbouk.charts.ChartProgressBar
import me.itangqi.waveloadingview.WaveLoadingView
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
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
    private lateinit var mAdView: AdView
    private final var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataLoader()

        MobileAds.initialize(this)
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Add loaded")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Log.d(TAG, adError.message)
            }

            override fun onAdOpened() {
                Log.d(TAG, "Add opened")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Add clicked")
            }

            override fun onAdClosed() {
                Log.d(TAG, "Add closed")
            }
        }

        manageNotifications()

        fillAchievementList()
        fillAchievments()

        fillDrinksList()
        val currentDayInRow = waterInfo.getDayInRow()
        if (waterInfo.dayPassed(getFormula(profile.sex)(profile.weight, profile.actTime))) {
            profile.currentExp += 50
        }
        if (currentDayInRow < waterInfo.getDayInRow()) {
            profile.money += 1
        }
        dataSaver()
        showTutorial()
    }

    override fun onStop() {
        dataSaver()
        super.onStop()
    }

    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        mAdView.destroy()
        manageNotifications()
        super.onDestroy()
    }

    override fun onResume() {
        dataLoader()
        val currentDayInRow = waterInfo.getDayInRow()
        if (waterInfo.dayPassed(getFormula(profile.sex)(profile.weight, profile.actTime))) {
            profile.currentExp += 50
        }
        if (currentDayInRow < waterInfo.getDayInRow()) {
            profile.money += 1
        }
        lvlCheck()
        updateViews()
        updateInfo()
        clearDrinksInfo()
        clearDrinksChronologyInfo()
        fillDrinksInfo()
        fillDrinksChronologyInfo()
        updateAchievments()
        updateGraph()
        super.onResume()
        mAdView.resume()
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
            5,
            R.drawable.achievement_100_day_n,
            R.drawable.achievement_100_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 100 }
        achievementList.add(achievement)
        achievement = Achievement(
            5, R.string.ach_day_365,
            R.string.ach_day_365_des,
            2500,
            false,
            10,
            R.drawable.achievement_365_day_n,
            R.drawable.achievement_365_day
        ) { dayInRowRecord: Int -> dayInRowRecord >= 365 }
        achievementList.add(achievement)
        achievement = Achievement(
            6, R.string.ach_all,
            R.string.ach_all_des,
            500,
            true,
            5,
            R.drawable.achievement_hidden,
            R.drawable.achievement_all_drinks
        ) { drinks: Int -> drinks == drinkList.size }
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
            5,
            R.drawable.achievement_hidden,
            R.drawable.achievement_30_lvl
        ) { level: Int -> level >= 30 }
        achievementList.add(achievement)
        achievement = Achievement(
            11, R.string.ach_lvl_50,
            R.string.ach_lvl_50_des,
            0,
            true,
            10,
            R.drawable.achievement_hidden,
            R.drawable.achievement_50_lvl
        ) { level: Int -> level >= 50 }
        achievementList.add(achievement)
        achievement = Achievement(
            12, R.string.ach_alco,
            R.string.ach_alco_des,
            250,
            true,
            5,
            R.drawable.achievement_hidden,
            R.drawable.achievement_alco
        ) { unused: Int -> unused == 1 }
        achievementList.add(achievement)
        achievement = Achievement(
            13, R.string.ach_coffee,
            R.string.ach_coffee_des,
            250,
            true,
            5,
            R.drawable.achievement_hidden,
            R.drawable.achievement_coffee
        ) { unused: Int -> unused == 1 }
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
                                getText(achievement.nameResource).toString() + "\n" + getText(
                                    achievement.descriptionResource
                                ).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
                6.toByte() -> {
                    var drinksAmount = 0
                    for (drink in drinkList) {
                        if (waterInfo.getDrinkAmount(drink.id) > 0) {
                            drinksAmount += 1
                        }
                    }
                    if (achievement.isUpdated(drinksAmount).first) {
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
                                getText(achievement.nameResource).toString() + "\n" + getText(
                                    achievement.descriptionResource
                                ).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
                in 7 until 12 -> {
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
                                getText(achievement.nameResource).toString() + "\n" + getText(
                                    achievement.descriptionResource
                                ).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
                12.toByte() -> {
                    val achState = achievement.isUpdated(waterInfo.checkMonthUnusage(5))
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
                                getText(achievement.nameResource).toString() + "\n" + getText(
                                    achievement.descriptionResource
                                ).toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            toastedList.addAchievement(achievement.id)
                        }
                        updated = true
                    }
                }
                13.toByte() -> {
                    val achState = achievement.isUpdated(waterInfo.checkMonthUnusage(3))
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
                                getText(achievement.nameResource).toString() + "\n" + getText(
                                    achievement.descriptionResource
                                ).toString(),
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
            findViewById<TextView>(R.id.moneyField).text = getString(
                R.string.int_number,
                profile.money
            )
            updateAchievments()
        }
    }

    private fun lvlCheck() {
        val expToLvlUp = { x: Int -> x * 50 + 50}
        val currentLvl = profile.lvl
        while (expToLvlUp(profile.lvl) <= profile.currentExp) {
            profile.currentExp -= expToLvlUp(profile.lvl)
            profile.lvl += 1
        }
        profile.money += profile.lvl - currentLvl
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
                        Toast.makeText(
                            this,
                            getText(achievement.descriptionResource),
                            Toast.LENGTH_SHORT
                        ).show()
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

            val iconLayout = ImageView(this)
            iconLayout.setImageResource(drink.resourceId)

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
            mParams.height = 50.dpToPixels(this).toInt()
            mParams.width = 50.dpToPixels(this).toInt()
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
                10.dpToPixels(this).toInt(), 5.dpToPixels(this).toInt(), 0.dpToPixels(
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

    fun fillDrinksChronologyInfo() {
        val chronology = waterInfo.getChronology()
        if (chronology.size == 0) {
            findViewById<TextView>(R.id.chronologyWarning).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.chronologyWarning).visibility = View.GONE
            for (triple in chronology) {
                val cardLayout = CardView(this)
                cardLayout.layoutParams = LinearLayout.LayoutParams(
                    90.dpToPixels(this).toInt(),
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                cardLayout.radius = 0F
                cardLayout.elevation = 0F
                val verticalLayout1 = LinearLayout(this)
                var vparams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                verticalLayout1.layoutParams = vparams
                verticalLayout1.orientation = LinearLayout.VERTICAL
                verticalLayout1.gravity = Gravity.CENTER_VERTICAL

                val iconLayout = LinearLayout(this)
                iconLayout.setBackgroundResource(drinkList[triple.first].resourceId)
                iconLayout.orientation = LinearLayout.VERTICAL
                val text = TextView(this)
                text.setText(drinkList[triple.first].name)
                text.maxLines = 1
                text.isSingleLine = true
                text.textSize = 12F
                text.ellipsize = TextUtils.TruncateAt.END
                text.gravity = Gravity.CENTER

                val text2 = TextView(this)
                text2.text = getString(
                    R.string.drink_amount,
                    triple.second / 1000.0
                )
                text2.maxLines = 1
                text2.isSingleLine = true
                text2.setTextColor(ContextCompat.getColor(this, R.color.blue_number))
                text2.textSize = 12F
                text2.setTypeface(null, Typeface.BOLD)
                text2.gravity = Gravity.CENTER

                val text3 = TextView(this)
                val df = SimpleDateFormat("HH:mm")
                text3.text = df.format(triple.third)
                text3.maxLines = 1
                text3.isSingleLine = true
    //            text3.setTextColor(ContextCompat.getColor(this, R.color.black))
                text3.textSize = 12F
                text3.setTypeface(null, Typeface.BOLD)
                text3.gravity = Gravity.CENTER

                verticalLayout1.addView(text3)
                verticalLayout1.addView(iconLayout)
                verticalLayout1.addView(text)
                verticalLayout1.addView(text2)


                cardLayout.addView(verticalLayout1)
                findViewById<LinearLayout>(R.id.todayDrinksChronology).addView(cardLayout)

                val mParams: LinearLayout.LayoutParams = iconLayout.layoutParams as LinearLayout.LayoutParams
                mParams.height = 70.dpToPixels(this).toInt()
                mParams.width = 70.dpToPixels(this).toInt()
                mParams.gravity = Gravity.CENTER
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
                    0.dpToPixels(this).toInt(), 5.dpToPixels(this).toInt(), 0.dpToPixels(
                        this
                    ).toInt(), 5.dpToPixels(this).toInt()
                )
                verticalLayout1.layoutParams = listViewParams
                verticalLayout1.postInvalidate()
            }
        }
    }

    fun clearDrinksInfo() {
        findViewById<LinearLayout>(R.id.todayDrinks1).removeAllViews()
        findViewById<LinearLayout>(R.id.todayDrinks2).removeAllViews()
    }

    fun clearDrinksChronologyInfo() {
        findViewById<LinearLayout>(R.id.todayDrinksChronology).removeAllViews()
    }

    fun openWaterStat(view: View) {
        dataSaver()
        startActivity(Intent(this, WaterActivity::class.java))
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
        findViewById<ImageView>(R.id.avatarHatMain).setImageResource(profile.hat.resourceId)
        findViewById<ImageView>(R.id.avatarMaskMain).setImageResource(profile.mask.resourceId)
    }

    fun updateInfo() {
        findViewById<TextView>(R.id.waterInfoView).text = if (waterInfo.getCurrentWater().toFloat() / 1000 > 0) {
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
            floor(
                (waterInfo.getCurrentWater().toDouble() / 1000 / getFormula(profile.sex)(
                    profile.weight,
                    profile.actTime
                )) * 100
            ).toInt()
        }
        updateAvatar(percent)
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
        val millis = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
        val formatter = SimpleDateFormat("dd/MM")
        var i = 1
        for (waterAmountStored in waterInfo.getLastWeekStat().reversed()) {
            var waterAmount = waterAmountStored
            if (waterAmount / 1000f > getFormula(profile.sex)(profile.weight, profile.actTime)) {
                waterAmount = (getFormula(profile.sex)(profile.weight, profile.actTime) * 1000).toInt()
            } else if (waterAmount < 0) {
                waterAmount = 0
            }
            val date = Date(millis - 86400000L * (7 - i))
            val data = BarData(
                formatter.format(date), waterAmount / 1000f, getString(
                    R.string.drink_amount,
                    waterAmount / 1000f
                )
            )
            dataList.add(data)
            i += 1
        }
        val mChart = findViewById<View>(R.id.ChartProgressBar) as ChartProgressBar
        mChart.setDataList(dataList as ArrayList<BarData>?)
        mChart.setMaxValue(getFormula(profile.sex)(profile.weight, profile.actTime))
        mChart.build()
    }

    fun openSettings(view: View) {
        dataSaver()
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun openAvatar(view: View) {
        dataSaver()
        startActivity(Intent(this, AvatarActivity::class.java))
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
        val timeFromPrevAdvert = System.currentTimeMillis() + TimeZone.getDefault().rawOffset - profile.lastAdvertShow
        val threeHours: Long = 60 * 60 * 2 * 1000
        if (timeFromPrevAdvert > threeHours) {
            findViewById<TextView>(R.id.advertNotification).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.advertNotification).visibility = View.GONE
            val timer: CountDownTimer = object : CountDownTimer(threeHours - timeFromPrevAdvert, 1000) {
                override fun onTick(l: Long) {}
                override fun onFinish() {
                    findViewById<TextView>(R.id.advertNotification).visibility = View.VISIBLE
                }
            }
            timer.start()
        }
    }

    fun swapDailyInfoView(view: View) {
        if (findViewById<LinearLayout>(R.id.todayDrinksGeneral).visibility != View.GONE) {
            findViewById<LinearLayout>(R.id.todayDrinksGeneral).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.chronologyLayout).visibility = View.VISIBLE
            findViewById<Button>(R.id.dailyInfoSwapButton).text = getString(R.string.chronology_string)
        } else {
            findViewById<LinearLayout>(R.id.todayDrinksGeneral).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.chronologyLayout).visibility = View.GONE
            findViewById<Button>(R.id.dailyInfoSwapButton).text = getString(R.string.simple_string)
        }
    }

    private fun showTutorial() {
        val pref = getSharedPreferences("dotFlexWaterPref", MODE_PRIVATE)
        if (!pref.getBoolean("didShowTutorial", false)){
            MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.waterInfoHolder)
                    .setPrimaryText(getString(R.string.tutorial_drink))
                    .setSecondaryText(getString(R.string.tutorial_drink_description))
                    .setBackButtonDismissEnabled(true)
                    .setPromptFocal(RectanglePromptFocal())
                    .setPromptStateChangeListener { prompt, state ->
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                                || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){

                            showCoinTutorial()
                        }
                    }
                    .show()
        }
    }

    private fun showCoinTutorial() {
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.coinHolder)
                .setPrimaryText(getString(R.string.tutorial_coins))
                .setSecondaryText(getString(R.string.tutorial_coins_description))
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                            || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                        showExpTutorial()
                    }
                }
                .show()
    }

    private fun showExpTutorial() {
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.lvlView)
                .setPrimaryText(getString(R.string.tutorial_exp))
                .setSecondaryText(getString(R.string.tutorial_exp_description))
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                            || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                        showSettingsTutorial()
                    }
                }
                .show()
    }

    private fun showSettingsTutorial() {
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.settingsButton)
                .setPrimaryText(getString(R.string.settings_heading))
                .setSecondaryText(getString(R.string.tutorial_settings_description))
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                            || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                        showStatTutorial()
                    }
                }
                .show()
    }

    private fun showStatTutorial() {
        findViewById<ScrollView>(R.id.scrollView2).post {
            findViewById<ScrollView>(R.id.scrollView2).smoothScrollTo(
                0,
                findViewById<CardView>(R.id.statHolder).top
            )
        }
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.statHolder)
                .setPrimaryText(getString(R.string.stat_section))
                .setSecondaryText(getString(R.string.tutorial_stat_description))
                .setBackButtonDismissEnabled(true)
                .setPromptFocal(RectanglePromptFocal())
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                            || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                        showChangeTutorial()
                    }
                }
                .show()
    }

    private fun showChangeTutorial() {
        val pref = getSharedPreferences("dotFlexWaterPref", MODE_PRIVATE)
        findViewById<ScrollView>(R.id.scrollView2).post { findViewById<ScrollView>(R.id.scrollView2).smoothScrollTo(0, findViewById<CardView>(R.id.todayDrinksHolder).top) }
        MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.dailyInfoSwapButton)
                .setPrimaryText(getString(R.string.tutorial_change))
                .setSecondaryText(getString(R.string.tutorial_change_description))
                .setBackButtonDismissEnabled(true)
                .setPromptStateChangeListener { prompt, state ->
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                            || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                        val prefEditor = pref.edit()
                        prefEditor.putBoolean("didShowTutorial", true)
                        prefEditor.apply()
                        findViewById<ScrollView>(R.id.scrollView2).post { findViewById<ScrollView>(R.id.scrollView2).smoothScrollTo(0, 0) }
                    }
                }
                .show()
    }


    private fun manageNotifications() {
        createNotificationChannel()
        cancelNotifications()
        if (profile.notificationStart == 1L) {
            if (profile.wakeUp != -1L || profile.bedtime != -1L) {
                val currentTime = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
                var currentBedTime = profile.bedtime
                if (profile.bedtime < profile.wakeUp) {
                    currentBedTime += 86400000L
                }
                when (profile.notificationFreq) {
                    Frequency.THIRTY_MINS -> {
                        if (currentTime % 86400000L + 30 * 60000 > profile.wakeUp) {
                            if (currentTime % 86400000L + 30 * 60000 < currentBedTime) {
                                createNotification(30 * 60000)
                            } else {
                                createNotification(86400000L - currentTime % 86400000L + profile.wakeUp)
                            }
                        } else {
                            createNotification(profile.wakeUp - currentTime % 86400000L)
                        }
                    }
                    Frequency.HOUR -> {
                        if (currentTime % 86400000L + 60 * 60000 > profile.wakeUp) {
                            if (currentTime % 86400000L + 60 * 60000 < currentBedTime) {
                                createNotification(60 * 60000)
                            } else {
                                createNotification(86400000L - currentTime % 86400000L + profile.wakeUp)
                            }
                        } else {
                            createNotification(profile.wakeUp - currentTime % 86400000L)
                        }
                    }
                    Frequency.TWO_HOURS -> {
                        if (currentTime % 86400000L + 120 * 60000 > profile.wakeUp) {
                            if (currentTime % 86400000L + 120 * 60000 < currentBedTime) {
                                createNotification(120 * 60000)
                            } else {
                                createNotification(86400000L - currentTime % 86400000L + profile.wakeUp)
                            }
                        } else {
                            createNotification(profile.wakeUp - currentTime % 86400000L)
                        }
                    }
                    Frequency.FIVE_HOURS -> {
                        if (currentTime % 86400000L + 300 * 60000 > profile.wakeUp) {
                            if (currentTime % 86400000L + 300 * 60000 < currentBedTime) {
                                createNotification(300 * 60000)
                            } else {
                                createNotification(86400000L - currentTime % 86400000L + profile.wakeUp)
                            }
                        } else {
                            createNotification(profile.wakeUp - currentTime % 86400000L)
                        }
                    }
                    Frequency.BEDTIME -> {
                        // TODO()
//                    val currentTime = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
//                    if (currentTime % 86400000L > profile.wakeUp) {
//                        createNotification(profile.bedtime - currentTime % 86400000L - 10 * 60 * 1000)
//                    } else {
//                        createNotification(profile.wakeUp - currentTime % 86400000L - 10 * 60 * 1000)
//                    }
                    }
                    Frequency.MEAL -> {
                        // TODO()
//                    val currentTime = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
//                    var isNotificationSet = false
//                    for (mealTime in profile.meals) {
//                        if (mealTime.second < currentTime % 86400000L) {
//                            continue
//                        } else {
//                            createNotification(mealTime.second - currentTime % 86400000L - 15 * 60 * 1000)
//                            isNotificationSet = true
//                            break
//                        }
//                    }
//                    if (!isNotificationSet && profile.meals.size != 0) {
//                        createNotification(profile.meals[0].second - currentTime % 86400000L + 86400000L - 15 * 60 * 1000)
//                    }
                    }
                    Frequency.WORKOUT -> {
                        // TODO()
                    }
                    Frequency.OPTIMUM -> {
                        // TODO()
                    }
                }
            }
        }
    }

    fun createNotification(timeInMillis: Long) {
        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val currentTime = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
        alarmManager.set(AlarmManager.RTC_WAKEUP, currentTime + timeInMillis, pendingIntent)
    }

    private fun cancelNotifications() {
        val intent = Intent(this, ReminderBroadcast::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "WaterInfo"
            val descriptionText = "Water reminder notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("waterNotifications", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}