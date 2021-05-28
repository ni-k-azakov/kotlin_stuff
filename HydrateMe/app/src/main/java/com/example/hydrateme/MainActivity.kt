package com.example.hydrateme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hydrateme.waterfall.Achievment
import com.example.hydrateme.waterfall.DataStorage
import com.example.hydrateme.waterfall.WaterInfo
import me.itangqi.waveloadingview.WaveLoadingView
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var waterInfo: WaterInfo
    private lateinit var waterLoadingView: WaveLoadingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        waterLoadingView = findViewById(R.id.waveLoaderView)
        waterLoadingView.progressValue = 80
        waterLoadingView.bottomTitle = String.format("%d%%", 80)
        waterLoadingView.centerTitle = ""
        waterLoadingView.topTitle = ""

        var achievment = Achievment(0, "Первые шаги",
                "Есть контакт! Первый день выполнения нормы",
                100,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 1 }
        achievment = Achievment(1, "Уверенное начало",
                "3 дня подряд. Маленькими шагами к здоровому будущему.",
                150,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 3 }
        achievment = Achievment(2, "В здоровом теле",
                "10 дней без пропусков. Отличный результат!",
                200,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 10 }
        achievment = Achievment(3, "Полезная привычка",
                "40 дней без перерыва. Это уже вошло в привычку!",
                300, false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 40 }
        achievment = Achievment(4, "Просветлённый",
                "Ого! Целых 100 дней подряд. Вот это результат!",
                1000,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 100 }
        achievment = Achievment(5, "Тот, кого нельзя не называть",
                "Целый год! Вот это выдержка. Больше для вас нет преград!",
                2500,
                false,
                0) { dayInRowRecord: Int -> dayInRowRecord >= 365 }
        achievment = Achievment(6, "Любитель разнообразия",
                "Вы попробовали все возможные напитки!",
                500,
                true,
                0) { level: Int -> level >= 8 }
        achievment = Achievment(7, "Постижение основ",
                "Вы достигли 3-го уровня.",
                100,
                true,
                0) { level: Int -> level >= 3 }
        achievment = Achievment(8, "Самоучка",
                "Вы достигли 10-го уровня.",
                150,
                true,
                0) { level: Int -> level >= 10 }
        achievment = Achievment(9, "Старательный ученик",
                "Вы достигли 20-го уровня.",
                300,
                true,
                0) { level: Int -> level >= 20 }
        achievment = Achievment(10, "Уверенный любитель",
                "Вы достигли 30-го уровня.",
                500,
                true,
                0) { level: Int -> level >= 30 }
        achievment = Achievment(11, "Мудрый наставник",
                "Вы достигли 50-го уровня.",
                700,
                true,
                0) { level: Int -> level >= 50 }
        achievment = Achievment(12, "Трезвость- моё второе имя",
                "Месяц без алкоголя.",
                250,
                true,
                0) { dayInRow: Int -> dayInRow >= 30 }
        achievment = Achievment(12, "Спокойствие на максимум",
                "Месяц без кофе.",
                250,
                true,
                0) { dayInRow: Int -> dayInRow >= 30 }
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
}