package com.example.mytodoapp.notification

import com.example.mytodoapp.data.Todo
import com.example.mytodoapp.data.TodoRepeat
import java.util.Calendar
import java.util.GregorianCalendar

object TodoRepeatCalculator {

    fun getOccurrenceTime(
        todo: Todo,
        repeatIndex: Int
    ): Long? {
        if (repeatIndex < 0) return null

        val repeat = TodoRepeat.values()
            .firstOrNull { it.name == todo.repeatType }
            ?: TodoRepeat.NONE

        if (repeat == TodoRepeat.NONE) {
            if (repeatIndex > 0) return null
        } else {
            val repeatCount = todo.repeatCount

            if (repeatCount != null && repeatIndex > repeatCount) {
                return null
            }
        }

        val calendar = getStartCalendar(todo) ?: return null

        return runCatching {
            when (repeat) {
                TodoRepeat.NONE -> Unit

                TodoRepeat.DAILY -> {
                    calendar.add(Calendar.DAY_OF_MONTH, repeatIndex)
                }

                TodoRepeat.WEEKLY -> {
                    calendar.add(Calendar.WEEK_OF_YEAR, repeatIndex)
                }

                TodoRepeat.MONTHLY -> {
                    calendar.add(Calendar.MONTH, repeatIndex)
                }

                TodoRepeat.YEARLY -> {
                    calendar.add(Calendar.YEAR, repeatIndex)
                }
            }

            calendar.timeInMillis
        }.getOrNull()
    }

    private fun getStartCalendar(todo: Todo): Calendar? {
        val date = todo.dueDate ?: return null
        val minutes = todo.dueTimeMinutes ?: return null

        if (minutes !in 0..1439) return null

        val parts = date.split("-")
        if (parts.size != 3) return null

        val year = parts[0].toIntOrNull() ?: return null
        val month = parts[1].toIntOrNull() ?: return null
        val day = parts[2].toIntOrNull() ?: return null

        return runCatching {
            GregorianCalendar().apply {
                isLenient = false
                clear()

                set(
                    year,
                    month - 1,
                    day,
                    minutes / 60,
                    minutes % 60,
                    0
                )
                timeInMillis
            }
        }.getOrNull()
    }
}