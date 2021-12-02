package com.practice.planter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import com.practice.planter.model.AddPlantRequest
import com.practice.planter.model.Plant
import com.practice.planter.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddPlantActivity : AppCompatActivity() {
    private lateinit var dropdown: Spinner
    private lateinit var xIcon: ImageView
    private lateinit var addPhotoButton: ImageView
    private lateinit var addPhotoText: TextView
    private lateinit var plantNameEditText: EditText
    private lateinit var wateringQuantityEditText: EditText
    private lateinit var wateringTimeSpinner: Spinner
    private lateinit var saveButton : Button

    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val client = OkHttpClient()
    private val addPlantRequestAdapter = moshi.adapter(AddPlantRequest::class.java)

    val BASE_URL: String = "https://leafy-app-backend.herokuapp.com/"

    fun calculateWateringTime(quantity : Int, duration : String): Long {
        when(duration) {
            "Hours" -> return quantity * DateUtils.HOUR_IN_MILLIS
            "Days" -> return quantity * DateUtils.DAY_IN_MILLIS
            "Weeks" -> return quantity * DateUtils.WEEK_IN_MILLIS
            "Months" -> return quantity * DateUtils.DAY_IN_MILLIS * 30
        }
        return quantity * DateUtils.WEEK_IN_MILLIS
    }

    fun getImage() : String {
        val image = findViewById<ImageView>(R.id.addImageButton)
        val drawable : Drawable = image.drawable
        val baos = ByteArrayOutputStream()
        val bitmap = drawable.toBitmap()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val imageString = "data:image/jpeg;base64,${Base64.encodeToString(imageBytes, Base64.DEFAULT)}"
        return imageString
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plant)

        val sharedPref = getSharedPreferences("authPreferences", Context.MODE_PRIVATE)
        val session_token = sharedPref.getString("session_token", "blah")
        val refresh_token = sharedPref.getString("refresh_token", "blah")

        dropdown = findViewById(R.id.scheduleDuration)
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter.createFromResource(this, R.array.duration_options, R.layout.dropdown_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        dropdown.adapter = adapter

        xIcon = findViewById(R.id.xIcon)
        xIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addPhotoButton = findViewById(R.id.addImageButton)
        addPhotoText = findViewById(R.id.addImageText)
        var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        saveButton = findViewById(R.id.addPlantSaveButton)
        saveButton.setOnClickListener {
            plantNameEditText = findViewById(R.id.addPlantName)
            wateringQuantityEditText = findViewById(R.id.scheduleQuantity)
            wateringTimeSpinner = findViewById(R.id.scheduleDuration)
            val plantName = plantNameEditText.text.toString()
            val wateringQuantity = wateringQuantityEditText.text.toString().toInt()
            val wateringDuration = wateringTimeSpinner.selectedItem.toString()
            val wateringTimeMs = calculateWateringTime(wateringQuantity, wateringDuration)

            Log.d("image", getImage())
            Log.d("wateringtime", wateringTimeMs.toString())
            Log.d("name", plantName)

            val addPlantRequest = Request.Builder().url(BASE_URL + "plants/")
                .post(
                    addPlantRequestAdapter.toJson(AddPlantRequest(getImage(), wateringTimeMs, plantName, "plont"))
                        .toRequestBody(("application/json; charset=utf-8").toMediaType())
                )
                .addHeader("Authorization", "Bearer " + session_token)
                .build()
            client.newCall(addPlantRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.message?.let { it1 -> Log.d("post error", it1) }
                    throw IOException(e.message)
                }

                override fun onResponse(call: Call, response: Response) {


                    response.use {
                        val responseData = it.body!!.string()
                        Log.d("post response", responseData)
                        Log.d("response", responseData)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity( intent )
                    }
                }
            })
        }


    }
}