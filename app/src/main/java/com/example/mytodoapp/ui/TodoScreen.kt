package com.example.mytodoapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytodoapp.data.Todo
import com.example.mytodoapp.data.TodoCategory
import com.example.mytodoapp.data.TodoPriority
import com.example.mytodoapp.data.TodoSchedule

enum class TodoFilter {
    All,
    Active,
    Completed
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    todos: List<Todo>,
    onAddTodo: (String, String, TodoCategory?, TodoPriority, TodoSchedule) -> Unit,
    onDeleteTodo: (Todo) -> Unit,
    onCompletedChange: (Todo, Boolean) -> Unit,
    onEditTodo: (Todo, String, String) -> Unit
){
    var showAddDialog by remember { mutableStateOf(false) }

    var todoToEdit by remember { mutableStateOf<Todo?>(null) }

    var selectedFilter by remember { mutableStateOf(TodoFilter.All) }
    val filteredTodos = when(selectedFilter) {
        TodoFilter.All -> todos
        TodoFilter.Active -> todos.filter { todo -> !todo.isCompleted}
        TodoFilter.Completed -> todos.filter { todo -> todo.isCompleted }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            HomeBottomBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                },
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm công việc",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
//            item {
//                Surface(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 40.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    color = MaterialTheme.colorScheme.surfaceVariant
//                ) {
//                    Text(
//                        text = "Hôm nay",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(
//                                horizontal = 16.dp,
//                                vertical = 14.dp
//                            ),
//                        textAlign = TextAlign.Center,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }

            item {
                val totalCount = todos.size
                val totalActive = todos.count { !it.isCompleted }
                val totalCompleted = todos.count { it.isCompleted }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TodoFilterButton(
                        text = "Tất cả ($totalCount)",
                        selected = selectedFilter == TodoFilter.All,
                        onClick = {
                            selectedFilter = TodoFilter.All
                        },
                        modifier = Modifier.weight(1f)
                    )

                    TodoFilterButton(
                        text = "Chưa hoàn thành ($totalActive)",
                        selected = selectedFilter == TodoFilter.Active,
                        onClick = {
                            selectedFilter = TodoFilter.Active
                        },
                        modifier = Modifier.weight(1.9f)
                    )

                    TodoFilterButton(
                        text = "Đã hoàn thành ($totalCompleted)",
                        selected = selectedFilter == TodoFilter.Completed,
                        onClick = {
                            selectedFilter = TodoFilter.Completed
                        },
                        modifier = Modifier.weight(1.7f)
                    )
                }
            }

            if (filteredTodos.isEmpty()) {
                item {
                    val title = when (selectedFilter) {
                        TodoFilter.All ->
                            "Hôm nay bạn muốn làm gì?"

                        TodoFilter.Active ->
                            "Không có công việc cần làm!"

                        TodoFilter.Completed ->
                            "Chưa có công việc hoàn thành!"
                    }

                    EmptyTodoState(
                        title = title,
                        showHint = selectedFilter == TodoFilter.All,
                        modifier = Modifier.padding(
                            top = 100.dp,
                            bottom = 24.dp
                        )
                    )
                }
            }
            else {
                items(
                    items = filteredTodos,
                    key = { todo -> todo.id }
                ) { todo ->
                    TodoItem(
                        todo = todo,
                        onDelete = { onDeleteTodo(todo) },
                        onCompletedChange = { isCompleted -> onCompletedChange(todo, isCompleted) },
                        onEdit = { todoToEdit = todo }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddTodoDialog(
            onDismiss = {
                showAddDialog = false
            },
            onSave = { title, description, category, priority, schedule ->
                onAddTodo(title, description, category, priority, schedule)
                showAddDialog = false
            }
        )
    }

    val editingTodo = todoToEdit
    if(editingTodo != null){
        EditTodoDialog(
            todo = editingTodo,
            onDismiss = { todoToEdit = null },
            onSave = { title, description ->
                onEditTodo(editingTodo ,title, description)
                todoToEdit = null
            }
        )
    }
}

@Composable
private fun TodoFilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.semantics {
            this.selected = selected
        },
        shape = RoundedCornerShape(10.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.White
        },
        contentColor = if (selected) {
            Color.White
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color(0xFFD1D5DB)
            }
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 10.dp
            ),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun TodoScreenPreview(){
    MaterialTheme{
        TodoScreen(
            todos = listOf(

            ),
            onAddTodo = { _, _, _, _, _ -> },
            onDeleteTodo = {_ ->},
            onCompletedChange = {_, _ ->},
            onEditTodo = {_, _, _ ->},
        )
    }
}