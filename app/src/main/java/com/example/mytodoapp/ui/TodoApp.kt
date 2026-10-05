package com.example.mytodoapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytodoapp.viewmodel.TodoViewModel

@Composable
fun TodoApp() {
    val todoViewModel: TodoViewModel = viewModel()
    val todos by todoViewModel.todos.collectAsStateWithLifecycle()

    TodoScreen(
        todos = todos,
        onAddTodo = { title, description, category, priority, schedule ->
            todoViewModel.addTodo(
                title,
                description,
                category,
                priority,
                schedule
            )
        },
        onDeleteTodo = { todo ->
            todoViewModel.deleteTodo(todo)
        },
        onCompletedChange = { todo, isCompleted ->
            todoViewModel.setTodoCompleted(todo, isCompleted)
        },
        onEditTodo = { todo, title, description ->
            todoViewModel.editTodo(todo, title, description)
        }
    )
}