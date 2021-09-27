package com.example.glassgo

import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.glassgo.waterfall.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.io.*
import java.util.*

class AvatarActivity : AppCompatActivity() {
    private lateinit var profile: Profile
    private lateinit var waterInfo: WaterInfo
    private val avatarList: MutableList<Avatar> = mutableListOf()
    private val hatList: MutableList<Clothes> = mutableListOf()
    private val maskList: MutableList<Clothes> = mutableListOf()
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "AvatarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar)
        dataLoader()

        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(this,
                getString(R.string.reward_ad_unit_id),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        Log.d(TAG, "Ad was loaded.")
                        rewardedAd = ad
                        updateButton()
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d(TAG, p0.message)
                        rewardedAd = null
                    }
                }
        )
        rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
                rewardedAd = null
            }
        }


        fillAvatarList()
        fillHatList()
        fillMaskList()
        fillAvatarListView()
        fillHatListView()
        fillMaskListView()
        updateViews()
        updateButton()
    }

    private fun dataLoader() {
        profile = if (File(this.filesDir.absolutePath + "/profile_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
            inputStream.readObject() as Profile
        } else {
            Profile()
        }

        waterInfo = if (File(this.filesDir.absolutePath + "/water_info_debug.dat").exists()) {
            val inputStream = ObjectInputStream(FileInputStream(this.filesDir.absolutePath + "/water_info_debug.dat"))
            WaterInfo(inputStream.readObject() as DataStorage)
        } else {
            WaterInfo(DataStorage())
        }
    }

    override fun onPause() {
        dataSaver()
        super.onPause()
    }

    private fun dataSaver() {
        val outputStream = ObjectOutputStream(FileOutputStream(this.filesDir.absolutePath + "/profile_info_debug.dat"))
        outputStream.writeObject(profile)
    }

    fun fillAvatarList() {
        var avatar = Avatar(
                0,
                R.string.avatar_blue_drop,
                R.drawable.blob,
                R.drawable.blob_happy,
                R.drawable.blob_sad,
                R.drawable.blob_super_sad,
                R.string.avatar_base_des,
                ConditionType.NONE,
                0
        )
        avatarList.add(avatar)

        avatar = Avatar(
                1,
                R.string.avatar_pink_drop,
                R.drawable.blob_female,
                R.drawable.blob_female_happy,
                R.drawable.blob_female_sad,
                R.drawable.blob_female_super_sad,
                R.string.avatar_base_des,
                ConditionType.NONE,
                0
        )
        avatarList.add(avatar)

        avatar = Avatar(
                2,
                R.string.avatar_cat,
                R.drawable.cat,
                R.drawable.cat_happy,
                R.drawable.cat_sad,
                R.drawable.cat_super_sad,
                R.string.avatar_cat_des,
                ConditionType.MONEY,
                15
        )
        avatarList.add(avatar)

        avatar = Avatar(
                3,
                R.string.avatar_red_cat,
                R.drawable.cat_orange,
                R.drawable.cat_orange_happy,
                R.drawable.cat_orange_sad,
                R.drawable.cat_orange_super_sad,
                R.string.avatar_red_cat_des,
                ConditionType.ADVERT,
                18
        )
        avatarList.add(avatar)

        avatar = Avatar(
                4,
                R.string.avatar_mouse,
                R.drawable.mouse,
                R.drawable.mouse_happy,
                R.drawable.mouse_sad,
                R.drawable.mouse_super_sad,
                R.string.avatar_mouse_des,
                ConditionType.MONEY,
                15
        )
        avatarList.add(avatar)
    }

    fun fillHatList() {
        var hat = Clothes(
                0,
                R.string.nothing,
                R.drawable.nothing,
                ConditionType.NONE,
                0
        )
        hatList.add(hat)

        hat = Clothes(
                1,
                R.string.bow,
                R.drawable.bow,
                ConditionType.MONEY,
                5
        )
        hatList.add(hat)

        hat = Clothes(
                2,
                R.string.crown,
                R.drawable.crown,
                ConditionType.ADVERT,
                30
        )
        hatList.add(hat)
    }

    fun fillMaskList() {
        var mask = Clothes(
                0,
                R.string.nothing,
                R.drawable.nothing,
                ConditionType.NONE,
                0
        )
        maskList.add(mask)

        mask = Clothes(
                1,
                R.string.sunglasses,
                R.drawable.sunglasses,
                ConditionType.ADVERT,
                8
        )
        maskList.add(mask)

        mask = Clothes(
                2,
                R.string.eye_patch,
                R.drawable.eyeband,
                ConditionType.MONEY,
                5
        )
        maskList.add(mask)
    }

    fun fillHatListView() {
        for (hat in hatList) {
            val cardLayout = CardView(this)
            cardLayout.layoutParams = LinearLayout.LayoutParams(
                    170.dpToPixels(this).toInt(),
                    170.dpToPixels(this).toInt()
            )
            cardLayout.elevation = 2F
            cardLayout.radius = 20F
            cardLayout.setContentPadding(
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt()
            )
            (cardLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt()
            )
            val image = ImageView(this)
            image.setImageResource(hat.resourceId)
            image.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    90.dpToPixels(this).toInt()
            )

            val imageChooser = ImageView(this)
            if (!profile.availableHatIdList.contains(hat.id.toByte())) {
                imageChooser.setImageResource(android.R.drawable.ic_lock_lock)
            } else {
                if (profile.hat.id == hat.id) {
                    imageChooser.setImageResource(R.drawable.check)
                }
                cardLayout.setOnClickListener {
                    profile.hat = hat
                    clearHatView()
                    fillHatListView()
                    updateViews()
                }
            }

            imageChooser.layoutParams = ViewGroup.LayoutParams(
                    25.dpToPixels(this).toInt(),
                    25.dpToPixels(this).toInt()
            )

            val infoLayout = LinearLayout(this)
            infoLayout.orientation = LinearLayout.VERTICAL
            infoLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            infoLayout.gravity = Gravity.CENTER
            (infoLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    0,
                    95.dpToPixels(this).toInt(),
                    0,
                    0,
            )
            val textName = TextView(this)
            textName.gravity = Gravity.CENTER
            textName.setTypeface(null, Typeface.BOLD)
            textName.text = getString(hat.nameResource)
            textName.setTextColor(resources.getColor(R.color.blue_number))

            infoLayout.addView(textName)
            when (hat.conditionType) {
                ConditionType.NONE, ConditionType.ACHIEVEMENT -> {
                }
                ConditionType.MONEY -> {
                    if (!profile.availableHatIdList.contains(hat.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        buyButton.text = getString(R.string.int_number, hat.conditionValue)
                        val star = resources.getDrawable(R.drawable.star_icon)
                        star!!.mutate().setTint(resources.getColor(R.color.white))
                        buyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, star, null)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.backgroundTintList = resources.getColorStateList(R.color.blue_number)
                        buyButton.setTextColor(resources.getColor(R.color.white))
                        buyButton.minHeight = 0
                        buyButton.setPadding(
                                30.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt(),
                                20.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt()
                        )
                        buyButton.setOnClickListener {
                            if (profile.money >= hat.conditionValue) {
                                profile.money -= hat.conditionValue
                                profile.availableHatIdList.add(hat.id.toByte())
                                clearHatView()
                                fillHatListView()
                                updateViews()
                            }
                        }
                        infoLayout.addView(buyButton)
                    }
                }
                ConditionType.ADVERT -> {
                    if (!profile.availableHatIdList.contains(hat.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        buyButton.text = getString(R.string.int_number, hat.conditionValue)
                        val advert = resources.getDrawable(R.drawable.advert_icon)
                        advert!!.mutate().setTint(resources.getColor(R.color.white))
                        buyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, advert, null)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.backgroundTintList = resources.getColorStateList(R.color.blue_number)
                        buyButton.setTextColor(resources.getColor(R.color.white))
                        buyButton.minHeight = 0
                        buyButton.setPadding(
                                30.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt(),
                                20.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt()
                        )
                        buyButton.setOnClickListener {
                            if (profile.advertCoins >= hat.conditionValue) {
                                profile.advertCoins -= hat.conditionValue
                                profile.availableHatIdList.add(hat.id.toByte())
                                clearHatView()
                                fillHatListView()
                                updateViews()
                            }
                        }
                        infoLayout.addView(buyButton)
                    }
                }
                ConditionType.RATING -> {
                    if (!profile.availableHatIdList.contains(hat.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.isAllCaps = false
                        buyButton.text = "Rate"
                        infoLayout.addView(buyButton)
                    }
                }
            }
            cardLayout.addView(image)
            cardLayout.addView(imageChooser)
            cardLayout.addView(infoLayout)

            findViewById<LinearLayout>(R.id.avatarHatList).addView(cardLayout)
        }
    }

    fun fillMaskListView() {
        for (mask in maskList) {
            val cardLayout = CardView(this)
            cardLayout.layoutParams = LinearLayout.LayoutParams(
                    170.dpToPixels(this).toInt(),
                    170.dpToPixels(this).toInt()
            )
            cardLayout.elevation = 2F
            cardLayout.radius = 20F
            cardLayout.setContentPadding(
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt()
            )
            (cardLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt()
            )
            val image = ImageView(this)
            image.setImageResource(mask.resourceId)
            image.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    90.dpToPixels(this).toInt()
            )

            val imageChooser = ImageView(this)
            if (!profile.availableMaskIdList.contains(mask.id.toByte())) {
                imageChooser.setImageResource(android.R.drawable.ic_lock_lock)
            } else {
                if (profile.mask.id == mask.id) {
                    imageChooser.setImageResource(R.drawable.check)
                }
                cardLayout.setOnClickListener {
                    profile.mask = mask
                    clearMaskView()
                    fillMaskListView()
                    updateViews()
                }
            }

            imageChooser.layoutParams = ViewGroup.LayoutParams(
                    25.dpToPixels(this).toInt(),
                    25.dpToPixels(this).toInt()
            )

            val infoLayout = LinearLayout(this)
            infoLayout.orientation = LinearLayout.VERTICAL
            infoLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            infoLayout.gravity = Gravity.CENTER
            (infoLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    0,
                    95.dpToPixels(this).toInt(),
                    0,
                    0,
            )
            val textName = TextView(this)
            textName.gravity = Gravity.CENTER
            textName.setTypeface(null, Typeface.BOLD)
            textName.text = getString(mask.nameResource)
            textName.setTextColor(resources.getColor(R.color.blue_number))

            infoLayout.addView(textName)
            when (mask.conditionType) {
                ConditionType.NONE, ConditionType.ACHIEVEMENT -> {
                }
                ConditionType.MONEY -> {
                    if (!profile.availableMaskIdList.contains(mask.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        buyButton.text = getString(R.string.int_number, mask.conditionValue)
                        val star = resources.getDrawable(R.drawable.star_icon)
                        star!!.mutate().setTint(resources.getColor(R.color.white))
                        buyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, star, null)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.backgroundTintList = resources.getColorStateList(R.color.blue_number)
                        buyButton.setTextColor(resources.getColor(R.color.white))
                        buyButton.minHeight = 0
                        buyButton.setPadding(
                                30.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt(),
                                20.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt()
                        )
                        buyButton.setOnClickListener {
                            if (profile.money >= mask.conditionValue) {
                                profile.money -= mask.conditionValue
                                profile.availableMaskIdList.add(mask.id.toByte())
                                clearMaskView()
                                fillMaskListView()
                                updateViews()
                            }
                        }
                        infoLayout.addView(buyButton)
                    }
                }
                ConditionType.ADVERT -> {
                    if (!profile.availableMaskIdList.contains(mask.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        buyButton.text = getString(R.string.int_number, mask.conditionValue)
                        val advert = resources.getDrawable(R.drawable.advert_icon)
                        advert!!.mutate().setTint(resources.getColor(R.color.white))
                        buyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, advert, null)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.backgroundTintList = resources.getColorStateList(R.color.blue_number)
                        buyButton.setTextColor(resources.getColor(R.color.white))
                        buyButton.minHeight = 0
                        buyButton.setPadding(
                                30.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt(),
                                20.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt()
                        )
                        buyButton.setOnClickListener {
                            if (profile.advertCoins >= mask.conditionValue) {
                                profile.advertCoins -= mask.conditionValue
                                profile.availableMaskIdList.add(mask.id.toByte())
                                clearMaskView()
                                fillMaskListView()
                                updateViews()
                            }
                        }
                        infoLayout.addView(buyButton)
                    }
                }
                ConditionType.RATING -> {
                    if (!profile.availableMaskIdList.contains(mask.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.isAllCaps = false
                        buyButton.text = "Rate"
                        infoLayout.addView(buyButton)
                    }
                }
            }
            cardLayout.addView(image)
            cardLayout.addView(imageChooser)
            cardLayout.addView(infoLayout)

            findViewById<LinearLayout>(R.id.avatarMaskList).addView(cardLayout)
        }
    }

    fun fillAvatarListView() {
        for (avatar in avatarList) {
            val cardLayout = CardView(this)
            cardLayout.layoutParams = LinearLayout.LayoutParams(
                    170.dpToPixels(this).toInt(),
                    170.dpToPixels(this).toInt()
            )
            cardLayout.elevation = 2F
            cardLayout.radius = 20F
            cardLayout.setContentPadding(
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt()
            )
            (cardLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt(),
                    10.dpToPixels(this).toInt()
            )
            val image = ImageView(this)
            image.setImageResource(avatar.resourceId)
            image.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    90.dpToPixels(this).toInt()
            )

            val imageChooser = ImageView(this)
            if (!profile.availableAvatarIdList.contains(avatar.id.toByte())) {
                imageChooser.setImageResource(android.R.drawable.ic_lock_lock)
            } else {
                if (profile.avatar.id == avatar.id) {
                    imageChooser.setImageResource(R.drawable.check)
                }
                cardLayout.setOnClickListener {
                    profile.avatar = avatar
                    clearAvatarView()
                    fillAvatarListView()
                    updateViews()
                }
            }

            imageChooser.layoutParams = ViewGroup.LayoutParams(
                    25.dpToPixels(this).toInt(),
                    25.dpToPixels(this).toInt()
            )

            val infoLayout = LinearLayout(this)
            infoLayout.orientation = LinearLayout.VERTICAL
            infoLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            infoLayout.gravity = Gravity.CENTER
            (infoLayout.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                    0,
                    95.dpToPixels(this).toInt(),
                    0,
                    0,
            )
            val textName = TextView(this)
            textName.gravity = Gravity.CENTER
            textName.setTypeface(null, Typeface.BOLD)
            textName.text = getString(avatar.nameResource)
            textName.setTextColor(resources.getColor(R.color.blue_number))

            infoLayout.addView(textName)
            when (avatar.conditionType) {
                ConditionType.NONE, ConditionType.ACHIEVEMENT -> {
                    val textDescription = TextView(this)
                    textDescription.gravity = Gravity.CENTER
                    textDescription.text = getString(avatar.conditionDescriptionResource)
                    infoLayout.addView(textDescription)
                }
                ConditionType.MONEY -> {
                    if (!profile.availableAvatarIdList.contains(avatar.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        buyButton.text = getString(R.string.int_number, avatar.conditionValue)
                        val star = resources.getDrawable(R.drawable.star_icon)
                        star!!.mutate().setTint(resources.getColor(R.color.white))
                        buyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, star, null)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.backgroundTintList = resources.getColorStateList(R.color.blue_number)
                        buyButton.setTextColor(resources.getColor(R.color.white))
                        buyButton.minHeight = 0
                        buyButton.setPadding(
                                30.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt(),
                                20.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt()
                        )
                        buyButton.setOnClickListener {
                            if (profile.money >= avatar.conditionValue) {
                                profile.money -= avatar.conditionValue
                                profile.availableAvatarIdList.add(avatar.id.toByte())
                                clearAvatarView()
                                fillAvatarListView()
                                updateViews()
                            }
                        }
                        infoLayout.addView(buyButton)
                    } else {
                        val textDescription = TextView(this)
                        textDescription.gravity = Gravity.CENTER
                        textDescription.text = getString(avatar.conditionDescriptionResource)
                        infoLayout.addView(textDescription)
                    }
                }
                ConditionType.ADVERT -> {
                    if (!profile.availableAvatarIdList.contains(avatar.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        buyButton.text = getString(R.string.int_number, avatar.conditionValue)
                        val advert = resources.getDrawable(R.drawable.advert_icon)
                        advert!!.mutate().setTint(resources.getColor(R.color.white))
                        buyButton.setCompoundDrawablesWithIntrinsicBounds(null, null, advert, null)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.backgroundTintList = resources.getColorStateList(R.color.blue_number)
                        buyButton.setTextColor(resources.getColor(R.color.white))
                        buyButton.minHeight = 0
                        buyButton.setPadding(
                                30.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt(),
                                20.dpToPixels(this).toInt(),
                                5.dpToPixels(this).toInt()
                        )
                        buyButton.setOnClickListener {
                            if (profile.advertCoins >= avatar.conditionValue) {
                                profile.advertCoins -= avatar.conditionValue
                                profile.availableAvatarIdList.add(avatar.id.toByte())
                                clearAvatarView()
                                fillAvatarListView()
                                updateViews()
                            }
                        }
                        infoLayout.addView(buyButton)
                    } else {
                        val textDescription = TextView(this)
                        textDescription.gravity = Gravity.CENTER
                        textDescription.text = getString(avatar.conditionDescriptionResource)
                        infoLayout.addView(textDescription)
                    }
                }
                ConditionType.RATING -> {
                    if (!profile.availableAvatarIdList.contains(avatar.id.toByte())) {
                        val buyButton = Button(this)
                        buyButton.text = getString(avatar.conditionDescriptionResource)
                        buyButton.gravity = Gravity.CENTER
                        buyButton.isAllCaps = false
                        buyButton.text = "Rate"
                        infoLayout.addView(buyButton)
                    } else {
                        val textDescription = TextView(this)
                        textDescription.gravity = Gravity.CENTER
                        textDescription.text = getString(avatar.conditionDescriptionResource)
                        infoLayout.addView(textDescription)
                    }
                }
            }
            cardLayout.addView(image)
            cardLayout.addView(imageChooser)
            cardLayout.addView(infoLayout)

            findViewById<LinearLayout>(R.id.avatarList).addView(cardLayout)
        }
    }


    fun clearAvatarView() {
        findViewById<LinearLayout>(R.id.avatarList).removeAllViews()
    }

    fun clearHatView() {
        findViewById<LinearLayout>(R.id.avatarHatList).removeAllViews()
    }

    fun clearMaskView() {
        findViewById<LinearLayout>(R.id.avatarMaskList).removeAllViews()
    }

    fun updateViews() {
        findViewById<TextView>(R.id.personName).text = profile.name
        findViewById<TextView>(R.id.moneyField).text = getString(R.string.int_number, profile.money)
        findViewById<TextView>(R.id.gemField).text = getString(R.string.int_number, profile.advertCoins)
        findViewById<ImageView>(R.id.avatar).setImageResource(profile.avatar.resourceId)
        findViewById<ImageView>(R.id.avatarMask).setImageResource(profile.mask.resourceId)
        findViewById<ImageView>(R.id.avatarHat).setImageResource(profile.hat.resourceId)
        findViewById<TextView>(R.id.achievementAmountView).text = getString(
                R.string.trophy_amount,
                profile.completedAchievmentsIdList.size
        )
        findViewById<TextView>(R.id.highestScoreView).text = getString(
                R.string.highest_score,
                waterInfo.getHighestScore()
        )
        findViewById<TextView>(R.id.lvlView).text = getString(R.string.lvl_info, profile.lvl)
        val expToLvlUp = { x: Int -> x * 50 + 50}
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = expToLvlUp(profile.lvl)
        progressBar.progress = profile.currentExp
        findViewById<TextView>(R.id.progressTextInfo).text = getString(
                R.string.progress_text, profile.currentExp, expToLvlUp(
                profile.lvl
        )
        )
        findViewById<TextView>(R.id.avatarAmount).text = getString(R.string.int_from_int, profile.availableAvatarIdList.size, avatarList.size)
        findViewById<TextView>(R.id.maskAmount).text = getString(R.string.int_from_int, profile.availableMaskIdList.size - 1, maskList.size - 1)
        findViewById<TextView>(R.id.hatAmount).text = getString(R.string.int_from_int, profile.availableHatIdList.size - 1, hatList.size - 1)
    }

    private fun updateButton() {
        val advertButton = findViewById<Button>(R.id.advertButton)
        advertButton.isEnabled = false
        advertButton.setOnClickListener {
            if (rewardedAd != null) {
                rewardedAd?.show(this) {
                    profile.advertCoins += it.amount
                    profile.lastAdvertShow = System.currentTimeMillis() + TimeZone.getDefault().rawOffset
                    updateButton()
                    updateViews()
                    Log.d(TAG, "User earned the reward.")
                }
            } else {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
            }
        }
        var timeFromPrevAdvert = System.currentTimeMillis() + TimeZone.getDefault().rawOffset - profile.lastAdvertShow
        val threeHours: Long = 60 * 60 * 2 * 1000
        if (timeFromPrevAdvert > threeHours) {
            timeFromPrevAdvert = threeHours
        }
        val timer: CountDownTimer = object : CountDownTimer(threeHours - timeFromPrevAdvert, 1000) {
            override fun onTick(l: Long) {
                val hours = (l / 1000 / 3600).toInt()
                val minutes = ((l / 1000 / 60) % 60).toInt()
                val seconds = ((l / 1000) % 60).toInt()
                advertButton.text = "${hours.toString().padStart(2, '0')}" +
                        ":${minutes.toString().padStart(2, '0')}" +
                        ":${seconds.toString().padStart(2, '0')}"
            }
            override fun onFinish() {
                advertButton.text = "+1"
                if (rewardedAd != null) {
                    advertButton.isEnabled = true
                }
            }
        }
        timer.start()
    }
}