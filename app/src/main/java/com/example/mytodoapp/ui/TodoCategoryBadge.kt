package com.example.mytodoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytodoapp.data.TodoCategory

@Composable
fun TodoCategoryBadge(
    category: TodoCategory,
    modifier: Modifier = Modifier
) {
    val colorBackground = when (category) {
        TodoCategory.EVENT -> Color(0xFFF2A74B)
        TodoCategory.PERSONAL -> Color(0xFF278C7A)
        TodoCategory.WORK -> Color(0xFFF24956)
        TodoCategory.FAVORITES -> Color(0xFFF36D87)
    }

    val iconBackground = when (category) {
        TodoCategory.EVENT -> Icons.Default.Event
        TodoCategory.PERSONAL -> Icons.Default.Person
        TodoCategory.WORK -> Icons.Default.Work
        TodoCategory.FAVORITES -> Icons.Default.Favorite
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorBackground)
            .heightIn(min = 32.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = iconBackground,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = category.displayName,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}