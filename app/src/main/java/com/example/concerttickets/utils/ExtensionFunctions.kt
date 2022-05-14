package com.example.concerttickets.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.toMonth(): String {

    return when (this) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09"-> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> ""
    }
}

fun String.isValidDate(): Int {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    val currentDate = dateFormat.format(Date())
    return try {
        val firstDate: Date? = dateFormat.parse(this)
        val secondDate: Date? = dateFormat.parse(currentDate)
        val compare = firstDate?.compareTo(secondDate)
        if (compare != null) {
            when {
                compare > 0 -> 1
                compare < 0 -> -1
                else -> 2
            }
        } else 0
    } catch (e: ParseException) {
        e.printStackTrace()
        0
    }
}
