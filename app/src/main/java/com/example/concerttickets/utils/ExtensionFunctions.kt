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

fun String.isValidDate(): Boolean {
    val sdf = SimpleDateFormat("MM.dd.yyyy", Locale.ENGLISH)

    val currentDate = sdf.format(Date())

    try {
        val firstDate: Date? = sdf.parse(this)
        val secondDate: Date? = sdf.parse(currentDate)

        val cmp = firstDate?.compareTo(secondDate)

        return if (cmp != null) {
            when {
                cmp > 0 ->
                    true
                cmp < 0 ->
                    false
                else ->
                    true
            }
        } else false
    } catch (e: ParseException) {
        e.printStackTrace()
        return false
    }
}
