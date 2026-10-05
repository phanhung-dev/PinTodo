package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.mytodoapp.R
import com.example.mytodoapp.data.TodoPriority

fun TodoPriority.iconColor(): Color = when (this) {
    TodoPriority.HIGH -> Color(0xFFEF2020)
    TodoPriority.MEDIUM -> Color(0xFFBF5700)
    TodoPriority.LOW -> Color(0xFF2962FF)
    TodoPriority.NONE -> Color(0xFF6B7280)
}

@Composable
fun PriorityPicker(
    selectedPriority: TodoPriority,
    onPrioritySelected: (TodoPriority) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val gapPx = with(LocalDensity.current) {
        8.dp.roundToPx()
    }

    val positionProvider = remember(gapPx) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                // Canh cạnh phải menu với cạnh phải nút cờ.
                val x = anchorBounds.right - popupContentSize.width

                // Đặt menu phía trên nút cờ.
                val y = anchorBounds.top -
                        popupContentSize.height - gapPx

                return IntOffset(
                    x = x.coerceIn(
                        0,
                        (windowSize.width - popupContentSize.width)
                            .coerceAtLeast(0)
                    ),
                    y = y.coerceIn(
                        0,
                        (windowSize.height - popupContentSize.height)
                            .coerceAtLeast(0)
                    )
                )
            }
        }
    }

    Box(modifier = modifier) {
        Surface(
            onClick = { expanded = !expanded },
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF3F4F6)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_flag),
                    contentDescription =
                        "Mức ưu tiên: ${selectedPriority.displayName}",
                    tint = selectedPriority.iconColor(),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        if (expanded) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF3F4F6)
                ) {
                    Column(
                        modifier = Modifier
                            .width(220.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        TodoPriority.values().forEach { priority ->
                            Surface(
                                onClick = {
                                    onPrioritySelected(priority)
                                    expanded = false
                                },
                                color = Color.Transparent,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .heightIn(min = 48.dp)
                                        .padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        ),
                                    horizontalArrangement =
                                        Arrangement.spacedBy(12.dp),
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            R.drawable.ic_flag
                                        ),
                                        contentDescription = null,
                                        tint = priority.iconColor(),
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Text(
                                        text = priority.displayName,
                                        color = Color(0xFF1F2937),
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}