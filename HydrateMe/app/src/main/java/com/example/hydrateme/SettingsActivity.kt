package com.example.hydrateme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.hydrateme.waterfall.Avatar
import com.example.hydrateme.waterfall.ConditionType
import com.example.hydrateme.waterfall.Profile
import java.io.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var profile: Profile
    private val avatarList: MutableList<Avatar> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        dataLoader()
        updateInfo()
        fillAvatarList()
        findViewById<ViewPager2>(R.id.avatarViewPager).adapter = ViewPagerAdapter(avatarList, 0)
        findViewById<ViewPager2>(R.id.avatarViewPager).apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        findViewById<ViewPager2>(R.id.avatarViewPager).registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position == profile.avatar.id) {
                        findViewById<Button>(R.id.selectButton).visibility = View.INVISIBLE
                        findViewById<TextView>(R.id.selectStatus).visibility = View.VISIBLE
                    } else {
                        findViewById<Button>(R.id.selectButton).visibility = View.VISIBLE
                        findViewById<Button>(R.id.selectButton).setOnClickListener {
                            profile.avatar = avatarList[position]
                            findViewById<Button>(R.id.selectButton).visibility = View.INVISIBLE
                            findViewById<TextView>(R.id.selectStatus).visibility = View.VISIBLE
                        }
                        findViewById<TextView>(R.id.selectStatus).visibility = View.INVISIBLE
                    }
                    super.onPageSelected(position)
                }
            }
        )
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
        findViewById<TextView>(R.id.moneyField).text = getString(R.string.int_number, profile.money)
    }

    fun fillAvatarList() {
        var avatar = Avatar(
            0,
            R.string.avatar_blue_drop,
            R.drawable.drop,
            R.string.avatar_base_des,
            ConditionType.NONE,
            0
        )
        avatarList.add(avatar)

        avatar = Avatar(
            1,
            R.string.avatar_pink_drop,
            R.drawable.drop_female,
            R.string.avatar_base_des,
            ConditionType.NONE,
            0
        )
        avatarList.add(avatar)
    }

    private class ViewPagerAdapter(val avatarList: MutableList<Avatar>, var currentAvatarId: Int) : RecyclerView.Adapter<PagerVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
            PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))

        override fun getItemCount(): Int = avatarList.size

        override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
            findViewById<ConstraintLayout>(R.id.avatarHolder).setBackgroundResource(avatarList[position].resourceId)
            findViewById<TextView>(R.id.avatarName).setText(avatarList[position].nameResource)
            findViewById<TextView>(R.id.conditionText).setText(avatarList[position].conditionDescriptionResource)
            if (currentAvatarId == avatarList[position].id) {
                findViewById<ProgressBar>(R.id.avatarProgressBar).visibility = View.INVISIBLE
            } else {
                findViewById<ProgressBar>(R.id.avatarProgressBar).visibility = View.INVISIBLE
            }
        }


    }

    class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}