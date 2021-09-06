package com.example.glassgo

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.glassgo.waterfall.Profile
import java.io.*

class FirstLaunchActivity : AppCompatActivity() {
    private var profile = Profile()
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_launch)
        pref = getSharedPreferences("dotFlexWaterPref", MODE_PRIVATE)
    }

    private fun dataSaver() {
        val outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
        outputStream.writeObject(profile)
    }

    fun nextStep(view: View) {
        when {
            findViewById<ConstraintLayout>(R.id.nameLayout).visibility == View.VISIBLE -> run {
                if (findViewById<EditText>(R.id.nameEditText).text.toString().isBlank()) {
                    findViewById<TextView>(R.id.noName).visibility = View.VISIBLE
                    return@run
                }
                findViewById<ConstraintLayout>(R.id.nameLayout).visibility = View.INVISIBLE
                findViewById<ConstraintLayout>(R.id.sexLayout).visibility = View.VISIBLE
            }
            findViewById<ConstraintLayout>(R.id.sexLayout).visibility == View.VISIBLE -> {
                findViewById<ConstraintLayout>(R.id.sexLayout).visibility = View.INVISIBLE
                findViewById<ConstraintLayout>(R.id.weightLayout).visibility = View.VISIBLE
            }
            findViewById<ConstraintLayout>(R.id.weightLayout).visibility == View.VISIBLE -> run {
                if (findViewById<EditText>(R.id.weightEditText).text.toString().isBlank()) {
                    findViewById<TextView>(R.id.noWeight).visibility = View.VISIBLE
                    return@run
                }
                findViewById<ConstraintLayout>(R.id.weightLayout).visibility = View.INVISIBLE
                findViewById<ConstraintLayout>(R.id.actTimeLayout).visibility = View.VISIBLE
            }
        }
    }

    fun finishAndSave(view: View) {
        if (findViewById<EditText>(R.id.activeTimeEditText).text.toString().isBlank()) {
            val actView = findViewById<TextView>(R.id.noActTime)
            actView.visibility = View.VISIBLE
            actView.text = getString(R.string.blank_field_launch)
            return
        }
        if (findViewById<EditText>(R.id.activeTimeEditText).text.toString().replace(",", ".").toFloat() > 24F) {
            val actView = findViewById<TextView>(R.id.noActTime)
            actView.visibility = View.VISIBLE
            actView.text = getString(R.string.act_time_error_launch)
            return
        }
        profile.name = findViewById<EditText>(R.id.nameEditText).text.toString()
        profile.actTime = findViewById<EditText>(R.id.activeTimeEditText).text.toString().replace(",", ".").toFloat()
        profile.sex = if (findViewById<RadioButton>(R.id.radioButton).isChecked) {
            0.toByte()
        } else {
            1.toByte()
        }
        profile.weight = findViewById<EditText>(R.id.weightEditText).text.toString().replace(",", ".").toFloat()
        dataSaver()
        pref!!.edit().putBoolean("firstRun", false).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}