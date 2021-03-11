package com.example.thatday2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
    }

    fun graphTransit(view: View) {
        val graphIntent = Intent(this, GraphActivity::class.java)
        startActivity(graphIntent)
    }
}
