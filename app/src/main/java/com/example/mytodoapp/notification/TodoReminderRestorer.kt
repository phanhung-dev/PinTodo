package com.example.mytodoapp.notification

import android.content.Context
import android.util.Log
import com.example.mytodoapp.data.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

object TodoReminderRestorer {

    suspend fun restore(context: Context) {
        withContext(Dispatchers.IO) {
            val appContext = context.applicationContext
            val scheduler = TodoAlarmScheduler(appContext)

            if (!scheduler.canScheduleExactAlarms()) {
                return@withContext
            }

            val database = TodoDatabase.getDatabase(appContext)

            val todos = database.todoDao()
                .getAllTodos()
                .first()

            todos.forEach { todo ->
                currentCoroutineContext().ensureActive()

                val success = scheduler.schedule(todo)

                if (!success) {
                    Log.w(
                        "TodoReminderRestorer",
                        "Không khôi phục được lịch của công việc ${todo.id}"
                    )
                }
            }
        }
    }
}