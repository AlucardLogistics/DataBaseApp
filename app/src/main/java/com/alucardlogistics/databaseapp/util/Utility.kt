package com.alucardlogistics.databaseapp.util

import java.text.SimpleDateFormat
import java.util.*

class Utility {

    //MUST USE LOWERCASE 'y'. API 23- can't use uppercase

    companion object {

        val currentTimeStamp: String?
            get() = try {
                val dateFormat =
                    SimpleDateFormat(
                        "MM-yyyy",
                        Locale.getDefault()
                    ) //MUST USE LOWERCASE 'y'. API 23- can't use uppercase
                dateFormat.format(Date())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        fun getMonthFromNumber(monthNumber: String?): String {
            return when (monthNumber) {
                "01" -> {
                    "Jan"
                }
                "02" -> {
                    "Feb"
                }
                "03" -> {
                    "Mar"
                }
                "04" -> {
                    "Apr"
                }
                "05" -> {
                    "May"
                }
                "06" -> {
                    "Jun"
                }
                "07" -> {
                    "Jul"
                }
                "08" -> {
                    "Aug"
                }
                "09" -> {
                    "Sep"
                }
                "10" -> {
                    "Oct"
                }
                "11" -> {
                    "Nov"
                }
                "12" -> {
                    "Dec"
                }
                else -> {
                    "Error"
                }
            }
        }
    }
}