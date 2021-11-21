package com.practice.planter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plant_card, parent, false)
        return MyViewHolder(view)    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val plant = plants[position]
        holder.plantName.text = plant.name

        var requestOptions = RequestOptions()
        Glide.with(holder.itemView.context)
            .load(plant.image)
            .apply( requestOptions.transform(CenterCrop() ,RoundedCorners(24)) )
            .into(holder.image)


    }

    override fun getItemCount(): Int {
        return plants.size
    }
}