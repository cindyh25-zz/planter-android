package com.practice.planter.model

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class Plant(
    val id : Int,
    val user_id : Int,
    val watering_time : Long,
    val name : String,
    val time_elapsed : Float,
    val start_time : String,
    val watering_date : String,
    val creation_date : String,
    val image : String,
)
