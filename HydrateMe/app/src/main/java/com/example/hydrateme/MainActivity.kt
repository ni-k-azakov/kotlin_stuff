package com.example.hydrateme

import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hydrateme.waterfall.Achievment
import com.example.hydrateme.waterfall.DataStorage
import com.example.hydrateme.waterfall.Profile
import com.example.hydrateme.waterfall.WaterInfo
import me.itangqi.waveloadingview.WaveLoadingView
import java.awt.font.TextAttribute
import java.io.*


class MainActivity : AppCompatActivity() {
    private lateinit var waterInfo: WaterInfo
    private lateinit var waterLoadingView: WaveLoadingView
    private val achievementList: MutableList<Achievment> = mutableListOf()
    private lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        waterLoadingView = findViewById(R.id.waveLoaderView)
        waterLoadingView.progressValue = 80
        waterLoadingView.bottomTitle = String.format("%d%%", 80)
        waterLoadingView.centerTitle = ""
        waterLoadingView.topTitle = ""

        profile = Profile()

        fillAchievmentList()
        fillAchievments()
    }

//    override fun onStop() {
//        super.onStop()
//        val outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/water_info.dat"))
//        outputStream.writeObject(waterInfo.storage)
//    }

    private fun dataLoader() {
        waterInfo = if (File(this.filesDir.absolutePath + "/per_info.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/water_info.dat"))
            WaterInfo(inputStream.readObject() as DataStorage)
        } else {
            WaterInfo(DataStorage())
        }
    }

    private fun fillAchievmentList() {
        var achievement = Achievment(0, "Первые шаги",
                "Есть контакт! Первый день выполнения нормы",
                100,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 1 }
        achievementList.add(achievement)
        achievement = Achievment(1, "Уверенное начало",
                "3 дня подряд. Маленькими шагами к здоровому будущему.",
                150,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 3 }
        achievementList.add(achievement)
        achievement = Achievment(2, "В здоровом теле",
                "10 дней без пропусков. Отличный результат!",
                200,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 10 }
        achievementList.add(achievement)
        achievement = Achievment(3, "Полезная привычка",
                "40 дней без перерыва. Это уже вошло в привычку!",
                300, false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 40 }
        achievementList.add(achievement)
        achievement = Achievment(4, "Просветлённый",
                "Ого! Целых 100 дней подряд. Вот это результат!",
                1000,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 100 }
        achievementList.add(achievement)
        achievement = Achievment(5, "Тот, кого нельзя не называть",
                "Целый год! Вот это выдержка. Больше для вас нет преград!",
                2500,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 365 }
        achievementList.add(achievement)
        achievement = Achievment(6, "Любитель разнообразия",
                "Вы попробовали все возможные напитки!",
                500,
                true,
                0) { level: Int -> level >= 8 }
        achievementList.add(achievement)
        achievement = Achievment(7, "Постижение основ",
                "Вы достигли 3-го уровня.",
                100,
                true,
                0) { level: Int -> level >= 3 }
        achievementList.add(achievement)
        achievement = Achievment(8, "Самоучка",
                "Вы достигли 10-го уровня.",
                150,
                true,
                0) { level: Int -> level >= 10 }
        achievementList.add(achievement)
        achievement = Achievment(9, "Старательный ученик",
                "Вы достигли 20-го уровня.",
                300,
                true,
                0) { level: Int -> level >= 20 }
        achievementList.add(achievement)
        achievement = Achievment(10, "Уверенный любитель",
                "Вы достигли 30-го уровня.",
                500,
                true,
                0) { level: Int -> level >= 30 }
        achievementList.add(achievement)
        achievement = Achievment(11, "Мудрый наставник",
                "Вы достигли 50-го уровня.",
                700,
                true,
                0) { level: Int -> level >= 50 }
        achievementList.add(achievement)
        achievement = Achievment(12, "Трезвость- моё второе имя",
                "Месяц без алкоголя.",
                250,
                true,
                0) { dayInRow: Int -> dayInRow >= 30 }
        achievementList.add(achievement)
        achievement = Achievment(13, "Спокойствие на максимум",
                "Месяц без кофе.",
                250,
                true,
                0) { dayInRow: Int -> dayInRow >= 30 }
        achievementList.add(achievement)
    }

    fun updateAchievments() {
        var updated = false
        for (achievement in achievementList) {
            when (achievement.id) {
                in 0 until 6 -> {
                    val achState = achievement.isUpdated(profile.dayInRow)
                    if (achState.first) {
                        profile.completedAchievmentsList.add(achievement)
                        profile.currentExp += achievement.exp
                        Toast.makeText(this, achievement.name, Toast.LENGTH_SHORT).show()
                        achievementList.remove(achievement)
                        updated = true
                    }
                }
                in 6 until 12 -> {
                    val achState = achievement.isUpdated(profile.lvl)
                    if (achState.first) {
                        profile.completedAchievmentsList.add(achievement)
                        profile.currentExp += achievement.exp
                        Toast.makeText(this, achievement.name, Toast.LENGTH_SHORT).show()
                        achievementList.remove(achievement)
                        updated = true
                    }
                }
                in 12 until 14 -> {
                    val achState = achievement.isUpdated(profile.dayInRow)
                    if (achState.first) {
                        profile.completedAchievmentsList.add(achievement)
                        profile.currentExp += achievement.exp
                        Toast.makeText(this, achievement.name, Toast.LENGTH_SHORT).show()
                        achievementList.remove(achievement)
                        updated = true
                    }
                }
            }
        }
        if (updated) {
            lvlCheck()
            updateAchievments()
        }
    }

    private fun lvlCheck() {
        val expToLvlUp = { x: Int -> x * 50}
        if (expToLvlUp(profile.lvl) < profile.currentExp) {
            val currentExp = profile.currentExp
            profile.currentExp %= expToLvlUp(profile.lvl)
            profile.lvl = currentExp / profile.lvl
        }
    }

    private fun fillAchievments() {
        var i = 0
        for (achievement in profile.completedAchievmentsList) {
            val layout: LinearLayout = LinearLayout(this)
            layout.setBackgroundResource(R.drawable.check)
            val text = TextView(this)
            text.text = if (achievement.isSecret) "?" else achievement.name
            text.minLines = 2
            text.maxLines = 2
            text.ellipsize = TextUtils.TruncateAt.END
            text.gravity = Gravity.CENTER
            when (i % 4) {
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
                3 -> {
                    findViewById<LinearLayout>(R.id.achiv4).addView(layout)
                    findViewById<LinearLayout>(R.id.achiv4).addView(text)
                }
            }
            val mParams: ViewGroup.LayoutParams = layout.layoutParams as ViewGroup.LayoutParams
            mParams.height = 70
            mParams.width = 70
            layout.layoutParams = mParams
            layout.postInvalidate()

            val tParams: LinearLayout.LayoutParams = text.layoutParams as LinearLayout.LayoutParams
            tParams.setMargins(0, 0, 0, 35)
            text.layoutParams = tParams
            text.postInvalidate()

            i += 1
        }
        for (achievement in achievementList) {
            println(i)
            val layout = LinearLayout(this)
            layout.setBackgroundResource(R.drawable.cross)
            val text = TextView(this)
            text.text = if (achievement.isSecret) "?" else achievement.name
            text.minLines = 2
            text.maxLines = 2
            text.ellipsize = TextUtils.TruncateAt.END
            text.gravity = Gravity.CENTER
            when (i % 4) {
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
                3 -> {
                    findViewById<LinearLayout>(R.id.achiv4).addView(layout)
                    findViewById<LinearLayout>(R.id.achiv4).addView(text)
                }
            }
            val mParams: ViewGroup.LayoutParams = layout.layoutParams as ViewGroup.LayoutParams
            mParams.height = 70
            mParams.width = 70
            layout.layoutParams = mParams
            layout.postInvalidate()

            val tParams: LinearLayout.LayoutParams = text.layoutParams as LinearLayout.LayoutParams
            tParams.setMargins(0, 0, 0, 35)
            text.layoutParams = tParams
            text.postInvalidate()

            i += 1
        }
    }
}