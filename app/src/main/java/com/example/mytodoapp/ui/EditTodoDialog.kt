package com.example.mytodoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytodoapp.data.Todo
import com.example.mytodoapp.viewmodel.TodoViewModel

@Composable
fun EditTodoDialog(
    todo: Todo,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
){
    var title by remember(todo.id) { mutableStateOf(todo.title) }
    var description by remember(todo.id) { mutableStateOf(todo.description) }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = { Text("Sửa công việc") },

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
                    label = { Text("Tiêu đề") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Miêu tả") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    onSave(title.trim(), description.trim())
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
}