package com.practice.planter

import java.util.*

data class Plant(
    val id : Int,
    val user_id : Int,
    val watering_time : Int,
    val name : String,
    val time_elapsed : Int,
    val start_time : Date,
    val creation_date : Date,
    val image : String,
)
