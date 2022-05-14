package com.example.concerttickets.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Helper {
    companion object {

        //Function to check if it picked the same date
        fun formatDate(year: Int, month: Int, day: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

            return try {
                dateFormat.format(calendar.time)
            } catch (e: ParseException) {
                e.printStackTrace()
                "false"
            }
        }
    }
}