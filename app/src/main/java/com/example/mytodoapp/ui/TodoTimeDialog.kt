package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mytodoapp.data.TodoTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTimeDialog(
    initialTime: TodoTime?,
    onDismiss: () -> Unit,
    onConfirm: (TodoTime?) -> Unit
) {
    val currentTime = remember { Calendar.getInstance() }

    val timeState = rememberTimePickerState(
        initialHour = initialTime?.hour
            ?: currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = initialTime?.minute
            ?: currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    var showKeyboardInput by rememberSaveable {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    val red = Color(0xFFE5484D)
    val pink = Color(0xFFFFF1F2)
    val grayBackground = Color(0xFFF3F4F6)
    val gray = Color(0xFF6B7280)
    val darkText = Color(0xFF1F2937)

    val pickerColors = TimePickerDefaults.colors(
        containerColor = Color.White,

        clockDialColor = Color(0xFFFFF5F6),
        selectorColor = red,
        clockDialSelectedContentColor = Color.White,
        clockDialUnselectedContentColor = darkText,

        timeSelectorSelectedContainerColor = pink,
        timeSelectorUnselectedContainerColor = grayBackground,
        timeSelectorSelectedContentColor = red,
        timeSelectorUnselectedContentColor = darkText
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .widthIn(max = 360.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Chọn thời gian",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = darkText
                )

                if (showKeyboardInput) {
                    TimeInput(
                        state = timeState,
                        colors = pickerColors
                    )
                } else {
                    TimePicker(
                        state = timeState,
                        colors = pickerColors,
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            showKeyboardInput = !showKeyboardInput
                        }
                    ) {
                        Icon(
                            imageVector = if (showKeyboardInput) {
                                Icons.Default.AccessTime
                            } else {
                                Icons.Default.Keyboard
                            },
                            contentDescription = if (showKeyboardInput) {
                                "Chuyển sang mặt đồng hồ"
                            } else {
                                "Nhập giờ bằng bàn phím"
                            },
                            tint = gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.heightIn(min = 48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = grayBackground,
                            contentColor = Color(0xFF4B5563)
                        )
                    ) {
                        Text(
                            text = "Hủy",
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = {
                            focusManager.clearFocus()

                            onConfirm(
                                TodoTime(
                                    hour = timeState.hour,
                                    minute = timeState.minute
                                )
                            )
                        },
                        modifier = Modifier.heightIn(min = 48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Xong",
                            fontSize = 16.sp
                        )
                    }
                }

                if (initialTime != null) {
                    TextButton(
                        onClick = {
                            onConfirm(null)
                        }
                    ) {
                        Text(
                            text = "Bỏ thời gian",
                            color = red
                        )
                    }
                }
            }
        }
    }
}