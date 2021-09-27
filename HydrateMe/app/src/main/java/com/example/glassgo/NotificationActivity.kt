package com.example.glassgo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import com.example.glassgo.waterfall.Frequency
import com.example.glassgo.waterfall.Profile
import nl.joery.timerangepicker.TimeRangePicker
import java.io.*
import kotlin.math.log

class NotificationActivity : AppCompatActivity() {
    private lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        dataLoader()
        updateInfo()
        updateTimes()
        val picker = findViewById<TimeRangePicker>(R.id.picker)
        picker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                updateTimes()
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                updateTimes()
            }

            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {

            }
        })
        setStyle()
    }

    private fun dataLoader() {
        profile = if (File(this.filesDir.absolutePath + "/profile_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
            inputStream.readObject() as Profile
        } else {
            Profile()
        }
    }

    override fun onPause() {
        dataSaver()
        super.onPause()
    }

    fun dataSaver() {
        val switch = findViewById<Switch>(R.id.notifOffOn)
        if (switch.isChecked) {
            val picker = findViewById<TimeRangePicker>(R.id.picker)
            when (findViewById<RadioGroup>(R.id.radioFreq).checkedRadioButtonId) {
                R.id.radio30Min -> {
                    profile.notificationFreq = Frequency.THIRTY_MINS
                }
                R.id.radio1Hour -> {
                    profile.notificationFreq = Frequency.HOUR
                }
                R.id.radio2Hours -> {
                    profile.notificationFreq = Frequency.TWO_HOURS
                }
                R.id.radio5Hours -> {
                    profile.notificationFreq = Frequency.FIVE_HOURS
                }
                else -> {
                }
            }
            profile.wakeUp = picker.startTimeMinutes * 60000L
            profile.bedtime = picker.endTimeMinutes * 60000L
            profile.notificationStart = 1
        } else {
            profile.notificationStart = -1
        }
        var outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
        outputStream.writeObject(profile)
    }

    private fun updateInfo() {
        when (profile.notificationFreq) {
            Frequency.THIRTY_MINS -> {
                findViewById<RadioGroup>(R.id.radioFreq).check(R.id.radio30Min)
            }
            Frequency.HOUR -> {
                findViewById<RadioGroup>(R.id.radioFreq).check(R.id.radio1Hour)
            }
            Frequency.TWO_HOURS -> {
                findViewById<RadioGroup>(R.id.radioFreq).check(R.id.radio2Hours)
            }
            Frequency.FIVE_HOURS -> {
                findViewById<RadioGroup>(R.id.radioFreq).check(R.id.radio5Hours)
            }
            else -> {}
        }
        val switch = findViewById<Switch>(R.id.notifOffOn)
        switch.isChecked = profile.notificationStart != -1L
        val picker = findViewById<TimeRangePicker>(R.id.picker)
        picker.endTimeMinutes = (profile.bedtime / 60000).toInt()
        picker.startTimeMinutes = (profile.wakeUp / 60000).toInt()
        findViewById<RadioGroup>(R.id.radioFreq).isGone = !switch.isChecked
        findViewById<RadioGroup>(R.id.notifMessage).isGone = switch.isChecked
        picker.isGone = !switch.isChecked
        findViewById<LinearLayout>(R.id.time_layout).isGone = !switch.isChecked
        findViewById<ConstraintLayout>(R.id.constraintLayout2).isGone = !switch.isChecked
    }

    fun updateVisibility(view: View) {
        val switch = findViewById<Switch>(R.id.notifOffOn)
        findViewById<RadioGroup>(R.id.radioFreq).isGone = !switch.isChecked
        findViewById<RadioGroup>(R.id.notifMessage).isGone = switch.isChecked
        findViewById<TimeRangePicker>(R.id.picker).isGone = !switch.isChecked
        findViewById<LinearLayout>(R.id.time_layout).isGone = !switch.isChecked
        findViewById<ConstraintLayout>(R.id.constraintLayout2).isGone = !switch.isChecked
    }

    private fun setStyle() {
        val picker = findViewById<TimeRangePicker>(R.id.picker)
        picker.thumbColorAuto = true
        picker.thumbIconColor = Color.WHITE
        picker.thumbSizeActiveGrow = 1.2f
        picker.clockFace = TimeRangePicker.ClockFace.APPLE
        picker.hourFormat = TimeRangePicker.HourFormat.FORMAT_24
    }

    private fun updateTimes() {
        val picker = findViewById<TimeRangePicker>(R.id.picker)
        findViewById<TextView>(R.id.end_time).text = picker.endTime.toString()
        findViewById<TextView>(R.id.start_time).text = picker.startTime.toString()
    }

}