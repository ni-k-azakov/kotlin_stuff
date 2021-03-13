package com.example.thatday2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.thatday2.Processor.PeriodsInfo
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {
    private val periodsInfo = PeriodsInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        cycle_medium_number.text = "0"
        period_medium_number.text = periodsInfo.averagePeriodsDuration.toString()

    }

    fun graphTransit(view: View) {
        val graphIntent = Intent(this, GraphActivity::class.java)
        startActivity(graphIntent)
    }
}
