package com.example.mytodoapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.mytodoapp.R
import com.example.mytodoapp.data.TodoCategory
import com.example.mytodoapp.ui.theme.TodoGray
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip

@Composable
fun CategoryPicker(
    selectedCategory: TodoCategory?,
    onCategorySelected: (TodoCategory?) -> Unit,
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
                val x = if (layoutDirection == LayoutDirection.Ltr) {
                    anchorBounds.left
                } else {
                    anchorBounds.right - popupContentSize.width
                }

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

    BoxWithConstraints(modifier = modifier) {
        val menuWidth = maxWidth

        CategoryTile(
            category = selectedCategory,
            showArrow = true,
            expanded = expanded,
            onClick = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (expanded) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = {
                    expanded = false
                },
                properties = PopupProperties(
                    focusable = true
                )
            ) {
                Column(
                    modifier = Modifier
                        .width(menuWidth)
                        .heightIn(max = 240.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (selectedCategory != null) {
                        TextButton(
                            onClick = {
                                onCategorySelected(null)
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color.Black)
                        ) {
                            Text(
                                text = "Bỏ danh mục",
                                color = Color.White
                            )
                        }
                    }

                    TodoCategory.values()
                        .filter { it != selectedCategory }
                        .forEach { category ->
                        CategoryTile(
                            category = category,
                            onClick = {
                                onCategorySelected(category)
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryTile(
    category: TodoCategory?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showArrow: Boolean = false,
    expanded: Boolean = false
) {
    val colorBackground = when (category) {
        TodoCategory.EVENT -> Color(0xFFF2A74B)
        TodoCategory.PERSONAL -> Color(0xFF278C7A)
        TodoCategory.WORK -> Color(0xFFF24956)
        TodoCategory.FAVORITES -> Color(0xFFF36D87)
        null -> Color(0xFF4B5563)
    }

    val iconBackground = when (category) {
        TodoCategory.EVENT -> Icons.Default.Event
        TodoCategory.PERSONAL -> Icons.Default.Person
        TodoCategory.WORK -> Icons.Default.Work
        TodoCategory.FAVORITES -> Icons.Default.Favorite
        null -> null
    }

    val textColor = if (category != null) Color.White else TodoGray

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF3F4F6),
        contentColor = textColor
    ) {
        Box(
            modifier = Modifier.heightIn(min = 48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (category != null) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(colorBackground)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 10.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category?.displayName ?: "Danh mục",
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = textColor
                )

                if (iconBackground != null) {
                    Icon(
                        imageVector = iconBackground,
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (showArrow) {
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.Default.KeyboardArrowUp
                        },
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}