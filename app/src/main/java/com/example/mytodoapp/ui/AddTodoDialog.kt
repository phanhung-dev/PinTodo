package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytodoapp.data.TodoCategory
import com.example.mytodoapp.data.TodoDate
import com.example.mytodoapp.data.TodoPriority
import com.example.mytodoapp.data.TodoReminder
import com.example.mytodoapp.data.TodoRepeat
import com.example.mytodoapp.data.TodoSchedule
import com.example.mytodoapp.data.TodoTime
import com.example.mytodoapp.ui.theme.TodoText

@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, TodoCategory?, TodoPriority, TodoSchedule) -> Unit
){
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedCategory by rememberSaveable {
        mutableStateOf<TodoCategory?>(null)
    }

    var selectedPriority by rememberSaveable {
        mutableStateOf(TodoPriority.NONE)
    }

    var selectedDate by rememberSaveable {
        mutableStateOf<TodoDate?>(null)
    }

    var selectedTime by rememberSaveable {
        mutableStateOf<TodoTime?>(null)
    }

    var showScheduleDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedReminder by rememberSaveable {
        mutableStateOf(TodoReminder.NONE)
    }

    var selectedRepeat by rememberSaveable {
        mutableStateOf(TodoRepeat.NONE)
    }

    var selectedRepeatCount by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,

        title = {
            Text(
                text = "Thêm công việc",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TodoText
            )
        },

        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(
                            text = "Tiêu đề",
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = TodoText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            text = "Mô tả (không bắt buộc)",
                            fontSize = 16.sp
                        )
                    },
                    minLines = 4,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = TodoText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                AddTodoOptionsRow(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category ->
                        selectedCategory = category
                    },
                    selectedPriority = selectedPriority,
                    onPrioritySelected = { priority ->
                        selectedPriority = priority
                    },
                    selectedDate = selectedDate,
                    onCalendarClick = {
                        showScheduleDialog = true
                    },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    val schedule = TodoSchedule(
                        date = selectedDate,
                        time = selectedTime,
                        reminder = selectedReminder,
                        repeat = selectedRepeat,
                        repeatCount = selectedRepeatCount
                    )

                    onSave(
                        title.trim(),
                        description.trim(),
                        selectedCategory,
                        selectedPriority,
                        schedule
                    )
                },
                enabled = title.trim().length >= 3
            ) {
                Text("Lưu")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )

    if (showScheduleDialog) {
        TodoScheduleDialog(
            initialDate = selectedDate,
            initialTime = selectedTime,
            initialReminder = selectedReminder,
            initialRepeat = selectedRepeat,
            initialRepeatCount = selectedRepeatCount,
            onDismiss = {
                showScheduleDialog = false
            },
            onConfirm = { date, time, reminder, repeatOption, count ->
                selectedDate = date
                selectedTime = time
                selectedReminder = reminder
                selectedRepeat = repeatOption
                selectedRepeatCount = count
                showScheduleDialog = false
            }
        )
    }
}