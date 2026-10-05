package com.example.mytodoapp.data

data class TodoSchedule(
    val date: TodoDate? = null,
    val time: TodoTime? = null,
    val reminder: TodoReminder = TodoReminder.NONE,
    val repeat: TodoRepeat = TodoRepeat.NONE,
    val repeatCount: Int? = null
)