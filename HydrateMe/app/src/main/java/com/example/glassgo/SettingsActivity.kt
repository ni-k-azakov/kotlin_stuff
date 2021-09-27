package com.example.glassgo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.glassgo.waterfall.Profile
import java.io.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        dataLoader()
        updateInfo()
    }

    override fun onResume() {
        dataLoader()
        updateInfo()
        super.onResume()
    }

    override fun onPause() {
        dataSaver()
        super.onPause()
    }
    private fun dataLoader() {
        profile = if (File(this.filesDir.absolutePath + "/profile_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
            inputStream.readObject() as Profile
        } else {
            Profile()
        }
    }

    fun dataSaver() {
        profile.name = findViewById<EditText>(R.id.nameEditText).text.toString()
        profile.actTime = findViewById<EditText>(R.id.activeTimeEditText).text.toString().replace(",", ".").toFloat()
        profile.sex = if (findViewById<ToggleButton>(R.id.sexToggleButton).isChecked) {
            1.toByte()
        } else {
            0.toByte()
        }
        profile.weight = findViewById<EditText>(R.id.weightEditText).text.toString().replace(",", ".").toFloat()

        var outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
        outputStream.writeObject(profile)
    }

    fun updateInfo() {
        val nameView = findViewById<EditText>(R.id.nameEditText)
        nameView.setText(profile.name)
        val sexView = findViewById<ToggleButton>(R.id.sexToggleButton)
        sexView.isChecked = profile.sex != 0.toByte()
        val activeTimeView = findViewById<EditText>(R.id.activeTimeEditText)
        activeTimeView.setText(String.format("%.2f", profile.actTime))
        val weightView = findViewById<EditText>(R.id.weightEditText)
        weightView.setText(String.format("%.1f", profile.weight))
    }

    fun startAvatarActivity(view: View) {
        dataSaver()
        startActivity(Intent(this, AvatarActivity::class.java))
    }

    fun openPrivacyPolicy(view: View) {
        dataSaver()
        val address = Uri.parse("https://glassgo.flycricket.io/privacy.html")
        startActivity(Intent(Intent.ACTION_VIEW, address))
    }

    fun startNotificationActivity(view: View) {
        dataSaver()
        startActivity(Intent(this, NotificationActivity::class.java))
    }
}