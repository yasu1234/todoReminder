package com.kumaydevelop.todoreminder.Util

import java.util.*

object CalenderUtil {

    fun adjustNotifyTime(code: Int, date: Date) : Calendar {
        val calender = Calendar.getInstance()
        calender.time = date

        when(code) {
            0 -> calender
            1 -> calender.add(Calendar.MINUTE, -5)
            2 -> calender.add(Calendar.MINUTE, -10)
            3 -> calender.add(Calendar.MINUTE, -15)
            4 -> calender.add(Calendar.MINUTE, -30)
            5 -> calender.add(Calendar.HOUR, -1)
            else -> calender
        }

        return calender
    }
}