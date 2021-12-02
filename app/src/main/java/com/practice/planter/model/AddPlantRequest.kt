package com.practice.planter.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddPlantRequest (
    val image : String,
    val watering_time : Long,
    val name : String,
    val plant_tag : String,
)