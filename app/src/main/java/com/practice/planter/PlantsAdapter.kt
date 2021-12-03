package com.practice.planter

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.practice.planter.model.Plant
import com.practice.planter.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class PlantsAdapter(private val plants: List<Plant>) : RecyclerView.Adapter<PlantsAdapter.MyViewHolder>() {


    class MyViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image : ImageView = itemView.findViewById(R.id.songImage)
        val image : ImageView = itemView.findViewById(R.id.plantCardImage)
        val plantName : TextView = itemView.findViewById(R.id.plantCardName)
        val lastWatered : TextView = itemView.findViewById(R.id.plantCardLastWatered)
        val scheduleText : TextView = itemView.findViewById(R.id.plantCardSchedule)
        val button : Button = itemView.findViewById(R.id.plantCardButton)
        val card : CardView = itemView.findViewById(R.id.plantCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plant_card, parent, false)
        return MyViewHolder(view)    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val context = holder.itemView.context
        val sharedPref = context.getSharedPreferences("authPreferences", Context.MODE_PRIVATE)
        val session_token = sharedPref.getString("session_token", "blah")
        val plant = plants[position]
        holder.plantName.text = plant.name
        holder.scheduleText.text = "Every ${convertWateringSchedule(plant.watering_time)}"
        setText(holder.lastWatered, plant.start_time)

        var requestOptions = RequestOptions()
        Glide.with(context)
            .load(plant.image)
            .apply( requestOptions.transform(CenterCrop() ,RoundedCorners(24)) )
            .into(holder.image)

        holder.button.setOnClickListener {
            Log.d("clicked water", "clicked water")
            if (session_token != null) {
                waterPlant(plant.id, session_token, holder.lastWatered, null)
                setText(holder.lastWatered, plant.start_time)
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SinglePlantActivity::class.java)
                .apply {
                    putExtra("name", plant.name)
                    putExtra("image", plant.image)
                    putExtra("schedule", "Every ${convertWateringSchedule(plant.watering_time)}")
                    putExtra("last_watered", "Last watered ${formatRelativeWateringTime(plant.start_time)}")
                    putExtra("next_watering", "Water next ${formatRelativeWateringTime(plant.watering_date)}")
                    putExtra("id", plant.id)
                }
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return plants.size
    }

    fun setText(lastWateredTextView : TextView, date:String) {
        lastWateredTextView.text = "Last watered ${formatRelativeWateringTime(date)}"
    }



}