package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mytodoapp.data.TodoDate
import androidx.compose.foundation.clickable
import com.example.mytodoapp.data.TodoTime
import com.example.mytodoapp.data.TodoReminder
import com.example.mytodoapp.data.TodoRepeat

@Composable
fun TodoScheduleDialog(
    initialDate: TodoDate?,
    initialTime: TodoTime?,
    initialReminder: TodoReminder,
    initialRepeat: TodoRepeat,
    initialRepeatCount: Int?,
    onDismiss: () -> Unit,
    onConfirm: (
        TodoDate,
        TodoTime?,
        TodoReminder,
        TodoRepeat,
        Int?
    ) -> Unit
) {
    var draftDate by rememberSaveable(initialDate) {
        mutableStateOf(initialDate ?: TodoDate.today())
    }

    var draftTime by rememberSaveable(initialTime) {
        mutableStateOf(initialTime)
    }

    var showTimeDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var draftReminder by rememberSaveable(initialReminder) {
        mutableStateOf(initialReminder)
    }

    var showReminderDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var draftRepeat by rememberSaveable(initialRepeat) {
        mutableStateOf(initialRepeat)
    }

    var showRepeatDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var draftRepeatCount by rememberSaveable(
        initialRepeat,
        initialRepeatCount
    ) {
        mutableStateOf(
            if (initialRepeat == TodoRepeat.NONE) {
                null
            } else {
                initialRepeatCount
            }
        )
    }

    var showRepeatEndDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TodoCalendar(
                    selectedDate = draftDate,
                    onDateSelected = { date ->
                        draftDate = date
                    }
                )

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFF5F6)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        ScheduleOptionRow(
                            icon = Icons.Outlined.Schedule,
                            title = "Thời gian",
                            value = draftTime?.displayText() ?: "Chưa đặt",
                            onClick = {
                                showTimeDialog = true
                            },
                            valueColor = if (draftTime != null) {
                                Color(0xFFE5484D)
                            } else {
                                Color(0xFF6B7280)
                            }
                        )

                        ScheduleOptionRow(
                            icon = Icons.Outlined.NotificationsNone,
                            title = "Lời nhắc",
                            value = if (draftReminder == TodoReminder.NONE) {
                                "Không"
                            } else {
                                draftReminder.displayName
                            },
                            onClick = {
                                showReminderDialog = true
                            },
                            valueColor = if (draftReminder == TodoReminder.NONE) {
                                Color(0xFF6B7280)
                            } else {
                                Color(0xFFE5484D)
                            }
                        )

                        ScheduleOptionRow(
                            icon = Icons.Outlined.Repeat,
                            title = "Lặp lại",
                            value = if (draftRepeat == TodoRepeat.NONE) {
                                "Không"
                            } else {
                                draftRepeat.displayName
                            },
                            onClick = {
                                showRepeatDialog = true
                            },
                            valueColor = if (draftRepeat == TodoRepeat.NONE) {
                                Color(0xFF6B7280)
                            } else {
                                Color(0xFFE5484D)
                            }
                        )

                        if (draftRepeat != TodoRepeat.NONE) {
                            ScheduleOptionRow(
                                icon = Icons.Outlined.Repeat,
                                title = "Kết thúc",
                                value = draftRepeatCount?.let { count ->
                                    "Kết thúc sau $count lần"
                                } ?: "Không giới hạn",
                                onClick = {
                                    showRepeatEndDialog = true
                                },
                                valueColor = if (draftRepeatCount != null) {
                                    Color(0xFFE5484D)
                                } else {
                                    Color(0xFF6B7280)
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.End
                    )
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF3F4F6),
                            contentColor = Color(0xFF4B5563)
                        ),
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Text(
                            text = "Hủy",
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = {
                            onConfirm(draftDate, draftTime, draftReminder, draftRepeat, draftRepeatCount)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE5484D),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.heightIn(min = 48.dp)
                    ) {
                        Text(
                            text = "Xong",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
    if (showTimeDialog) {
        TodoTimeDialog(
            initialTime = draftTime,
            onDismiss = {
                showTimeDialog = false
            },
            onConfirm = { time ->
                draftTime = time
                showTimeDialog = false
            }
        )
    }

    if (showReminderDialog) {
        TodoReminderDialog(
            initialReminder = draftReminder,
            onDismiss = {
                showReminderDialog = false
            },
            onConfirm = { reminder ->
                draftReminder = reminder
                showReminderDialog = false
            }
        )
    }

    if (showRepeatDialog) {
        TodoRepeatDialog(
            initialRepeat = draftRepeat,
            onDismiss = {
                showRepeatDialog = false
            },
            onConfirm = { repeatOption ->
                draftRepeat = repeatOption

                if (repeatOption == TodoRepeat.NONE) {
                    draftRepeatCount = null
                }

                showRepeatDialog = false
            }
        )
    }

    if (showRepeatEndDialog && draftRepeat != TodoRepeat.NONE) {
        TodoRepeatEndDialog(
            initialCount = draftRepeatCount,
            onDismiss = {
                showRepeatEndDialog = false
            },
            onConfirm = { count ->
                draftRepeatCount = count
                showRepeatEndDialog = false
            }
        )
    }
}

@Composable
private fun ScheduleOptionRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: (() -> Unit)? = null,
    valueColor: Color = Color(0xFF6B7280)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .heightIn(min = 56.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = Color(0xFF1F2937)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = valueColor
        )
    }
}