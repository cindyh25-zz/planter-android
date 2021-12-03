package com.practice.planter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SinglePlantActivity : AppCompatActivity() {
    private lateinit var imageSection : ConstraintLayout
    private lateinit var image : ImageView
    private lateinit var name : TextView
    private lateinit var backButton : ImageView
    private lateinit var scheduleText : TextView
    private lateinit var lastWaterText : TextView
    private lateinit var nextWaterText : TextView
    private lateinit var waterButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_plant)

        val sharedPref = getSharedPreferences("authPreferences", Context.MODE_PRIVATE)
        val session_token = sharedPref.getString("session_token", "blah")

        val plantName = intent.extras?.getString("name")
        val imageURL = intent.extras?.getString("image")
        val scheduleString = intent.extras?.getString("schedule")
        val lastWateredString = intent.extras?.getString("last_watered")
        val nextWateredString = intent.extras?.getString("next_watering")
        val plantId = intent.extras?.getInt("id")

        imageSection = findViewById(R.id.plantImageSection)
        image = imageSection.findViewById(R.id.plantFullImage)
        name = imageSection.findViewById(R.id.singlePlantName)
        Glide.with(applicationContext)
            .load(imageURL)
            .centerCrop()
            .into(image)
        name.text = plantName

        backButton = findViewById(R.id.backIcon)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }

        scheduleText = findViewById(R.id.scheduleText)
        lastWaterText = findViewById(R.id.lastWateredtext)
        nextWaterText = findViewById(R.id.nextWateringText)
        scheduleText.text = scheduleString
        lastWaterText.text = lastWateredString
        nextWaterText.text = nextWateredString

        waterButton = findViewById(R.id.waterButton)
        waterButton.setOnClickListener {
            if (session_token != null && plantId != null) {
                waterPlant(plantId?.toInt(), session_token, lastWaterText, nextWaterText )
            }
        }
    }

}