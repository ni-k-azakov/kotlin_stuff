package com.example.thatday2

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView
import com.example.thatday2.Processor.DataStorage
import com.example.thatday2.Processor.PeriodsInfo
import kotlinx.android.synthetic.main.activity_calendar.*
import java.io.*
import java.util.*


class CalendarActivity : AppCompatActivity() {
    private lateinit var periodsInfo: PeriodsInfo
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        periodLoader()
        showPeriodInfo()

        calendarView = findViewById(R.id.periodCalendar)
        calendarView.setOnDayClickListener { eventDay ->
            val clickedDayCalendar: Calendar = eventDay.calendar
            val time = clickedDayCalendar.timeInMillis
            periodsInfo.addOrRemoveDate(time)
            Toast.makeText(this@CalendarActivity, periodsInfo.savedData.periodDays.size.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        val outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/per_info.dat"))
        outputStream.writeObject(periodsInfo.savedData)
    }

    fun graphTransit(view: View) {
        val graphIntent = Intent(this, GraphActivity::class.java)
        startActivity(graphIntent)
    }

    private fun periodLoader() {
        periodsInfo = if (File(this.filesDir.absolutePath + "/per_info.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/per_info.dat"))
            PeriodsInfo(inputStream.readObject() as DataStorage)
        } else {
            PeriodsInfo(DataStorage())
        }
    }

    private fun showPeriodInfo() {
        periodsInfo.updateStat()
        Toast.makeText(this@CalendarActivity, periodsInfo.savedData.periodsDurations.size.toString(), Toast.LENGTH_SHORT).show()
        cycle_medium_number.text = periodsInfo.averageCycleDuration.toString()
        period_medium_number.text = periodsInfo.averagePeriodsDuration.toString()
    }
}
