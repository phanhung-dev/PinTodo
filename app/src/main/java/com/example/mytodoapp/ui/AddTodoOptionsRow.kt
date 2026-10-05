package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mytodoapp.R
import com.example.mytodoapp.data.TodoCategory
import com.example.mytodoapp.ui.theme.TodoGray
import com.example.mytodoapp.data.TodoPriority
import com.example.mytodoapp.data.TodoDate

@Composable
fun AddTodoOptionsRow(
    selectedCategory: TodoCategory?,
    onCategorySelected: (TodoCategory?) -> Unit,
    selectedPriority: TodoPriority,
    onPrioritySelected: (TodoPriority) -> Unit,
    selectedDate: TodoDate?,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColors = ButtonDefaults.buttonColors(
        disabledContainerColor = Color(0xFFF3F4F6),
        disabledContentColor = TodoGray
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryPicker(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.weight(1f)
        )

        PriorityPicker(
            selectedPriority = selectedPriority,
            onPrioritySelected = onPrioritySelected
        )

        Button(
            onClick = onCalendarClick,
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3F4F6),
                contentColor = if (selectedDate != null) {
                    Color(0xFFE5484D)
                } else {
                    Color(0xFF6B7280)
                }
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = "Chọn ngày",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}