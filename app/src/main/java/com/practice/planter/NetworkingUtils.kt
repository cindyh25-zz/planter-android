package com.practice.planter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.TextView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun waterPlant(plantId:Int, session_token : String, textView: TextView, nextTextView : TextView?) {
    Log.d("plant id", plantId.toString())
    val client = OkHttpClient()
    val waterRequest = Request.Builder().url("https://leafy-app-backend.herokuapp.com/plants/water/${plantId}/")
        .post("".toRequestBody(("application/json; charset=utf-8").toMediaType()))
        .addHeader("Authorization", "Bearer " + session_token)
        .build()
    client.newCall(waterRequest).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.message?.let { Log.d("water failure", it) }
            throw IOException(e.message)
        }

        override fun onResponse(call: Call, response: Response) {

            response.use {
                val responseData = it.body!!.string()
                Log.d("response", responseData)

                val responseJSON = JSONObject(responseData)
                val newStartTime = responseJSON.get("start_time")
                val newNextWaterTime = responseJSON.get("watering_date")
                textView.text = "Last watered ${formatRelativeWateringTime(newStartTime as String)}"

                if (nextTextView != null) {
                    nextTextView.text = "Water next ${formatRelativeWateringTime(newNextWaterTime as String)}"
                }
            }
        }

    })
}

