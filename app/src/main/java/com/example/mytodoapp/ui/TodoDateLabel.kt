package com.example.mytodoapp.ui

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun todoDateLabel(
    dueDate: String?,
    dueTimeMinutes: Int?
): String? {
    if (dueDate == null) return null

    val formatter = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.ROOT
    )

    val today = formatter.format(Calendar.getInstance().time)

    val dateText = if (dueDate == today) {
        "Hôm nay"
    } else {
        val parts = dueDate.split("-")

        if (parts.size == 3) {
            "${parts[2]}/${parts[1]}"
        } else {
            dueDate
        }
    }

    if (dueTimeMinutes == null) return dateText

    val hour = dueTimeMinutes / 60
    val minute = dueTimeMinutes % 60

    val timeText = String.format(
        Locale.ROOT,
        "%02d:%02d",
        hour,
        minute
    )

    return "$dateText\n$timeText"
}