package com.example.mytodoapp.data

import java.util.Calendar
import java.io.Serializable

data class TodoDate(
    val year: Int,
    val month: Int,
    val day: Int
) : Serializable {
    companion object {
        fun today(): TodoDate {
            val calendar = Calendar.getInstance()

            return TodoDate(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1,
                day = calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}