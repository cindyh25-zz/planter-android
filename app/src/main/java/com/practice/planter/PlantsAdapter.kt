package com.practice.planter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop




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
        val plant = plants[position]
        holder.plantName.text = plant.name

        var requestOptions = RequestOptions()
        Glide.with(context)
            .load(plant.image)
            .apply( requestOptions.transform(CenterCrop() ,RoundedCorners(24)) )
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SinglePlantActivity::class.java)
                .apply {
                    putExtra("name", plant.name)
                    putExtra("image", plant.image)
                    putExtra("schedule", plant.watering_time)
                    putExtra("last_watered", plant.time_elapsed)
                }
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return plants.size
    }
}