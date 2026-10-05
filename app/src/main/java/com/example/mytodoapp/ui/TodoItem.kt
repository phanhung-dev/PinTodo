package com.example.mytodoapp.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mytodoapp.data.Todo
import com.example.mytodoapp.data.TodoCategory
import com.example.mytodoapp.data.TodoPriority
import androidx.compose.ui.res.painterResource
import com.example.mytodoapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TodoItem(
    todo: Todo,
    onDelete: () -> Unit,
    onCompletedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
){
    val priority = TodoPriority.values()
        .firstOrNull { it.name == todo.priority }
        ?: TodoPriority.NONE

    val dateLabel = todoDateLabel(
        dueDate = todo.dueDate,
        dueTimeMinutes = todo.dueTimeMinutes
    )

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { isChecked -> onCompletedChange(isChecked) }
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    textDecoration = if (todo.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )

                if(todo.description.isNotBlank()){
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        textDecoration = if(todo.isCompleted) { TextDecoration.LineThrough } else { TextDecoration.None }
                    )
                }

                val category = TodoCategory.values()
                    .firstOrNull { it.name == todo.category }

                if (category != null || dateLabel != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (category != null) {
                            TodoCategoryBadge(
                                category = category
                            )
                        }

                        if (dateLabel != null) {
                            Text(
                                text = dateLabel,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flag),
                        contentDescription = priority.displayName,
                        tint = priority.iconColor(),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Sửa công việc: ${todo.title}",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xóa công việc: ${todo.title}",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){
    MaterialTheme{
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TodoItem(Todo(
                id = 1L,
                title = "Học Jetpack Compose",
                description = "Tự viết TodoItem",
                category = "PERSONAL"
            ),
                onDelete = {},
                onCompletedChange = {},
                onEdit = {}
            )
            TodoItem(Todo(
                id = 2L,
                title = "Ôn Kotlin",
                isCompleted = true,
                category = "FAVORITES",
                priority = "HIGH"
            ),
                onDelete = {},
                onCompletedChange = {},
                onEdit = {}
            )
        }
    }
}
