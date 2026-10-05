package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytodoapp.data.TodoDate
import java.util.Calendar
import java.util.GregorianCalendar
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TodoCalendar(
    selectedDate: TodoDate?,
    onDateSelected: (TodoDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialDate = remember {
        selectedDate ?: TodoDate.today()
    }

    var displayedYear by rememberSaveable {
        mutableStateOf(initialDate.year)
    }

    var displayedMonth by rememberSaveable {
        mutableStateOf(initialDate.month)
    }

    val dates = remember(displayedYear, displayedMonth) {

        val calendar = GregorianCalendar(
            displayedYear,
            displayedMonth - 1,
            1
        )

        val leadingDays =
            (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7

        val daysInMonth =
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val totalCells =
            ((leadingDays + daysInMonth + 6) / 7) * 7

        calendar.add(Calendar.DAY_OF_MONTH, -leadingDays)

        List(totalCells) {
            val date = TodoDate(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1,
                day = calendar.get(Calendar.DAY_OF_MONTH)
            )

            calendar.add(Calendar.DAY_OF_MONTH, 1)
            date
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (displayedMonth == 1) {
                        displayedMonth = 12
                        displayedYear--
                    } else {
                        displayedMonth--
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Tháng trước",
                    tint = Color(0xFF6B7280)
                )
            }

            Text(
                text = "tháng $displayedMonth năm $displayedYear",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )

            IconButton(
                onClick = {
                    if (displayedMonth == 12) {
                        displayedMonth = 1
                        displayedYear++
                    } else {
                        displayedMonth++
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Tháng sau",
                    tint = Color(0xFF6B7280)
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                .forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
        }

        dates.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val isSelected = date == selectedDate
                    val isCurrentMonth =
                        date.year == displayedYear &&
                                date.month == displayedMonth

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            onClick = {
                                onDateSelected(date)
                                displayedYear = date.year
                                displayedMonth = date.month
                            },
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = if (isSelected) {
                                Color(0xFFE5484D)
                            } else {
                                Color.Transparent
                            }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = date.day.toString(),
                                    fontSize = 14.sp,
                                    color = when {
                                        isSelected -> Color.White
                                        isCurrentMonth -> Color(0xFF1F2937)
                                        else -> Color(0xFF9CA3AF)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 440
)
@Composable
private fun TodoCalendarPreview() {
    var selectedDate by remember {
        mutableStateOf<TodoDate?>(TodoDate(2026, 9, 29))
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                TodoCalendar(
                    selectedDate = selectedDate,
                    onDateSelected = {
                        selectedDate = it
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}