package com.example.mytodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytodoapp.data.Todo
import com.example.mytodoapp.data.TodoCategory
import com.example.mytodoapp.data.TodoDatabase
import com.example.mytodoapp.data.TodoDate
import com.example.mytodoapp.data.TodoPriority
import com.example.mytodoapp.data.TodoRepeat
import com.example.mytodoapp.data.TodoRepository
import com.example.mytodoapp.data.TodoSchedule
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import android.widget.Toast
import com.example.mytodoapp.notification.TodoAlarmScheduler

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TodoDatabase.getDatabase(application)
    private val repository = TodoRepository(database.todoDao())

    private val alarmScheduler = TodoAlarmScheduler(application)

    val todos: StateFlow<List<Todo>> =
        repository.getAllTodos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addTodo(
        title: String,
        description: String,
        category: TodoCategory?,
        priority: TodoPriority,
        schedule: TodoSchedule
    ) {
        val cleanedTitle = title.trim()

        if (cleanedTitle.length < 3) return

        val date = schedule.date ?: TodoDate.today()

        val dueDate = String.format(
                Locale.ROOT,
                "%04d-%02d-%02d",
                date.year,
                date.month,
                date.day
            )


        val dueTimeMinutes = schedule.time?.let {
            it.hour * 60 + it.minute
        }

        val repeat = schedule.repeat

        val repeatCount = if (repeat != TodoRepeat.NONE) {
            schedule.repeatCount
        } else {
            null
        }

        val newTodo = Todo(
            title = cleanedTitle,
            description = description.trim(),
            category = category?.name,
            priority = priority.name,
            repeatType = repeat.name,
            dueDate = dueDate,
            dueTimeMinutes = dueTimeMinutes,
            reminderMinutesBefore = schedule.reminder.minutesBefore,
            repeatCount = repeatCount
        )

        viewModelScope.launch {
            val savedId = repository.addTodo(newTodo)
            val savedTodo = newTodo.copy(id = savedId)
            updateReminder(savedTodo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
            alarmScheduler.cancel(todo.id)
        }
    }

    fun setTodoCompleted(
        todo: Todo,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            repository.updateCompleted(todo.id, isCompleted)
            updateReminder(
                todo.copy(isCompleted = isCompleted)
            )
        }
    }

    fun editTodo(
        todo: Todo,
        title: String,
        description: String
    ) {
        val cleanedTitle = title.trim()
        if (cleanedTitle.length < 3) return

        val cleanedDescription = description.trim()

        viewModelScope.launch {
            repository.editTodo(
                todo.id,
                cleanedTitle,
                cleanedDescription
            )
            updateReminder(
                todo.copy(
                    title = cleanedTitle,
                    description = cleanedDescription
                )
            )
        }
    }

    private fun updateReminder(todo: Todo) {
        val success = alarmScheduler.schedule(todo)

        if (!success) {
            Toast.makeText(
                getApplication<Application>(),
                "Chưa đặt được lời nhắc. Hãy kiểm tra ngày giờ và quyền Báo thức và lời nhắc.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}