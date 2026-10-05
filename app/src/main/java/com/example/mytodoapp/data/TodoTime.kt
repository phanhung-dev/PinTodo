package com.example.mytodoapp.data

import java.io.Serializable

data class TodoTime(
    val hour: Int,
    val minute: Int
) : Serializable {
    fun displayText(): String {
        val hourText = hour.toString().padStart(2, '0')
        val minuteText = minute.toString().padStart(2, '0')

        return "$hourText:$minuteText"
    }
}