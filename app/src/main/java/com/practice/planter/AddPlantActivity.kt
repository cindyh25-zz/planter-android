package com.practice.planter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts

class AddPlantActivity : AppCompatActivity() {
    private lateinit var dropdown : Spinner
    private lateinit var xIcon : ImageView
    private lateinit var addPhotoButton : ImageView
    private lateinit var addPhotoText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plant)

        dropdown = findViewById(R.id.scheduleDuration)
        val adapter :ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.duration_options, R.layout.dropdown_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        dropdown.adapter = adapter

        xIcon = findViewById(R.id.xIcon)
        xIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addPhotoButton = findViewById(R.id.addImageButton)
        addPhotoText = findViewById(R.id.addImageText)
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                var imageUri = data?.data
                addPhotoButton.setImageURI(imageUri)
                addPhotoText.text = ""
                addPhotoButton.clipToOutline = true
            }
        }
        addPhotoButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            resultLauncher.launch(gallery)
        }

    }
}