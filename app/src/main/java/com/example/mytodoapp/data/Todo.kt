package com.example.mytodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val category: String? = null,

    @ColumnInfo(defaultValue = "'NONE'")
    val priority: String = TodoPriority.NONE.name,

    val dueDate: String? = null,

    val dueTimeMinutes: Int? = null,

    val reminderMinutesBefore: Int? = null,

    @ColumnInfo(defaultValue = "'NONE'")
    val repeatType: String = TodoRepeat.NONE.name,

    val repeatCount: Int? = null
)



