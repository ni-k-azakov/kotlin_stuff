package com.example.glassgo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


class LaunchActivity : AppCompatActivity() {
    var pref: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_launch)
        pref = getSharedPreferences("dotFlexWaterPref", MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        if (!pref!!.contains("firstRun")) {
            pref!!.edit().putBoolean("firstRun", true).apply()
        }
        if (pref!!.getBoolean("firstRun", true)) {
            startActivity(Intent(this, FirstLaunchActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}