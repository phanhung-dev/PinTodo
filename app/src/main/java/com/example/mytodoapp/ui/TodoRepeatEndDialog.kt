package com.example.mytodoapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoRepeatEndDialog(
    initialCount: Int?,
    onDismiss: () -> Unit,
    onConfirm: (Int?) -> Unit
) {
    val maxCount = 999
    val startingCount = (initialCount ?: 1).coerceIn(1, maxCount)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = startingCount - 1
    )

    val scope = rememberCoroutineScope()

    // Lấy số nằm gần chính giữa vùng cuộn nhất.
    val selectedCount by remember(listState, startingCount) {
        derivedStateOf {
            val layout = listState.layoutInfo

            val center = (
                    layout.viewportStartOffset +
                            layout.viewportEndOffset
                    ) / 2

            val centerItem = layout.visibleItemsInfo.minByOrNull { item ->
                abs(item.offset + item.size / 2 - center)
            }

            centerItem?.let { it.index + 1 } ?: startingCount
        }
    }

    val red = Color(0xFFE5484D)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Kết thúc sau",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(
                                color = Color(0xFFFFF1F2),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 96.dp),
                        flingBehavior = rememberSnapFlingBehavior(
                            lazyListState = listState
                        )
                    ) {
                        items(
                            count = maxCount,
                            key = { index -> index + 1 }
                        ) { index ->
                            val number = index + 1
                            val distance = abs(number - selectedCount)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clickable {
                                        scope.launch {
                                            listState.animateScrollToItem(index)
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = number.toString(),
                                    modifier = Modifier.width(96.dp),
                                    textAlign = TextAlign.Center,
                                    fontSize = 24.sp,
                                    color = when (distance) {
                                        0 -> red
                                        1 -> Color(0xFF9CA3AF)
                                        else -> Color(0xFFD1D5DB)
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "Lần",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp),
                        fontSize = 20.sp,
                        color = red
                    )
                }

                TextButton(
                    onClick = {
                        onConfirm(null)
                    }
                ) {
                    Text(
                        text = "Không giới hạn",
                        color = Color(0xFF6B7280)
                    )
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.heightIn(min = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F4F6),
                    contentColor = Color(0xFF4B5563)
                )
            ) {
                Text("Hủy", fontSize = 16.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedCount)
                },
                enabled = !listState.isScrollInProgress,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.heightIn(min = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = red,
                    contentColor = Color.White
                )
            ) {
                Text("Xong", fontSize = 16.sp)
            }
        }
    )
}