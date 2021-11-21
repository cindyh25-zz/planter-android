package com.practice.planter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_plant)

        val plantName = intent.extras?.getString("name")
        val imageURL = intent.extras?.getString("image")

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
    }

}