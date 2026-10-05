package com.example.mytodoapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mytodoapp.data.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

class TodoAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val todoId = intent.getLongExtra("todo_id", 0L)
        if (todoId <= 0L) return

        val isEarly = intent.getBooleanExtra("is_early", false)
        val repeatIndex = intent.getIntExtra("repeat_index", 0)

        if (repeatIndex < 0) return

        val pendingResult = goAsync()
        val appContext = context.applicationContext

        CoroutineScope(Dispatchers.IO).launch {
            try {
                withTimeout(8.seconds) {
                    val scheduler = TodoAlarmScheduler(appContext)

                    val todo = TodoDatabase
                        .getDatabase(appContext)
                        .todoDao()
                        .getTodoById(todoId)

                    if (
                        todo == null ||
                        todo.isCompleted ||
                        todo.reminderMinutesBefore == null
                    ) {
                        scheduler.cancel(todoId)
                        return@withTimeout
                    }

                    val expectedAt = scheduler.getTriggerTime(
                        todo = todo,
                        repeatIndex = repeatIndex,
                        isEarly = isEarly
                    ) ?: return@withTimeout

                    val scheduledAt = intent.getLongExtra(
                        "trigger_at",
                        expectedAt
                    )

                    if (scheduledAt != expectedAt) {
                        return@withTimeout
                    }

                    val nextScheduled = scheduler.scheduleNext(
                        todo = todo,
                        isEarly = isEarly,
                        afterIndex = repeatIndex
                    )

                    if (!nextScheduled) {
                        Log.w(
                            "TodoRepeat",
                            "Chưa đặt được lượt tiếp theo: id=$todoId"
                        )
                    }

                    val dueAt = TodoRepeatCalculator.getOccurrenceTime(
                        todo = todo,
                        repeatIndex = repeatIndex
                    ) ?: return@withTimeout

                    if (
                        isEarly &&
                        dueAt <= System.currentTimeMillis()
                    ) {
                        return@withTimeout
                    }

                    val message = if (isEarly) {
                        "Sắp đến giờ: ${todo.title}"
                    } else {
                        todo.title
                    }

                    val type = if (isEarly) "early" else "due"

                    TodoNotificationHelper.show(
                        context = appContext,
                        tag = "todo_${todo.id}_$type",
                        title = "Todo App",
                        message = message
                    )
                }
            } catch (exception: Exception) {
                Log.e(
                    "TodoRepeat",
                    "Lỗi xử lý thông báo: id=$todoId",
                    exception
                )
            } finally {
                pendingResult.finish()
            }
        }
    }
}