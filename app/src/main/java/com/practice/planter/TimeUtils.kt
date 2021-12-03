package com.practice.planter

import android.text.format.DateUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

// Convert watering time in ms to a string "2 weeks"
fun convertWateringSchedule(wateringTimeMs : Long): String {
    val months = wateringTimeMs % (DateUtils.DAY_IN_MILLIS * 30)
    val weeks = wateringTimeMs % (DateUtils.WEEK_IN_MILLIS)
    val days = wateringTimeMs % (DateUtils.DAY_IN_MILLIS)
    val hours = wateringTimeMs % (DateUtils.HOUR_IN_MILLIS)

    if (months == 0.toLong()) {
        val n = (wateringTimeMs / DateUtils.DAY_IN_MILLIS * 30).toString()
        return "$n months"
    } else if (weeks == 0.toLong()) {
        val n = (wateringTimeMs / DateUtils.WEEK_IN_MILLIS).toString()
        return "$n weeks"
    } else if (days == 0.toLong()) {
        val n = (wateringTimeMs / DateUtils.DAY_IN_MILLIS).toString()
        return "$n days"
    } else {
        val n = (wateringTimeMs / DateUtils.HOUR_IN_MILLIS).toString()
        return "$n hours"
    }
}

// Convert date string to Date, returns relative time span string (in 2 days, 2 days ago)
fun formatRelativeWateringTime(lastWateredTime : String) : CharSequence {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS")
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    val value = dateFormat.parse(lastWateredTime)
    dateFormat.setTimeZone(TimeZone.getDefault())
    var lastWateredDate = dateFormat.format(value)
    val now = System.currentTimeMillis()
    val string = DateUtils.getRelativeTimeSpanString(dateFormat.parse(lastWateredDate).time, now,  DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL)
    return string
}