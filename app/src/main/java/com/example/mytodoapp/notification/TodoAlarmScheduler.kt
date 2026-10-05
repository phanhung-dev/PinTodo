package com.example.mytodoapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.net.toUri
import com.example.mytodoapp.data.Todo

class TodoAlarmScheduler(context: Context) {

    private val appContext = context.applicationContext

    private val alarmManager =
        appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun scheduleAt(
        todoId: Long,
        title: String,
        triggerAtMillis: Long,
        isEarly: Boolean,
        repeatIndex: Int = 0
    ): Boolean {
        if (triggerAtMillis <= System.currentTimeMillis()) {
            return false
        }

        if (!canScheduleExactAlarms()) {
            return false
        }

        val intent = createIntent(todoId, isEarly).apply {
            putExtra("todo_id", todoId)
            putExtra("todo_title", title)
            putExtra("is_early", isEarly)

            putExtra("repeat_index", repeatIndex)
            putExtra("trigger_at", triggerAtMillis)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        return try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            true
        } catch (_: SecurityException) {
            false
        }
    }

    fun cancel(todoId: Long) {
        listOf(true, false).forEach { isEarly ->
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                0,
                createIntent(todoId, isEarly),
                PendingIntent.FLAG_NO_CREATE or
                        PendingIntent.FLAG_IMMUTABLE
            )

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }

    fun schedule(todo: Todo): Boolean {
        cancel(todo.id)

        if (todo.isCompleted) return true

        val minutesBefore = todo.reminderMinutesBefore ?: return true

        if (todo.dueDate == null || todo.dueTimeMinutes == null) {
            return true
        }

        if (minutesBefore < 0) return false

        if (TodoRepeatCalculator.getOccurrenceTime(todo, 0) == null) {
            return false
        }

        val dueAlarm = findNextAlarm(
            todo = todo,
            isEarly = false
        )

        val earlyAlarm = if (minutesBefore > 0) {
            findNextAlarm(
                todo = todo,
                isEarly = true
            )
        } else {
            null
        }

        if (dueAlarm == null && earlyAlarm == null) {
            return true
        }

        if (!canScheduleExactAlarms()) return false

        var success = true

        if (dueAlarm != null) {
            success = scheduleAt(
                todoId = todo.id,
                title = todo.title,
                triggerAtMillis = dueAlarm.triggerAtMillis,
                isEarly = false,
                repeatIndex = dueAlarm.repeatIndex
            )
        }

        if (earlyAlarm != null) {
            val earlySuccess = scheduleAt(
                todoId = todo.id,
                title = todo.title,
                triggerAtMillis = earlyAlarm.triggerAtMillis,
                isEarly = true,
                repeatIndex = earlyAlarm.repeatIndex
            )

            success = success && earlySuccess
        }

        return success
    }

    fun scheduleNext(
        todo: Todo,
        isEarly: Boolean,
        afterIndex: Int
    ): Boolean {
        if (todo.isCompleted) return true

        val nextAlarm = findNextAlarm(
            todo = todo,
            isEarly = isEarly,
            afterIndex = afterIndex
        ) ?: return true

        return scheduleAt(
            todoId = todo.id,
            title = todo.title,
            triggerAtMillis = nextAlarm.triggerAtMillis,
            isEarly = isEarly,
            repeatIndex = nextAlarm.repeatIndex
        )
    }

    fun getTriggerTime(
        todo: Todo,
        repeatIndex: Int,
        isEarly: Boolean
    ): Long? {
        val minutesBefore = todo.reminderMinutesBefore ?: return null

        if (minutesBefore < 0) return null
        if (isEarly && minutesBefore == 0) return null

        val dueAt = TodoRepeatCalculator.getOccurrenceTime(
            todo = todo,
            repeatIndex = repeatIndex
        ) ?: return null

        return if (isEarly) {
            dueAt - minutesBefore.toLong() * 60_000L
        } else {
            dueAt
        }
    }

    private fun findNextAlarm(
        todo: Todo,
        isEarly: Boolean,
        afterIndex: Int = -1
    ): NextAlarm? {
        if (afterIndex == Int.MAX_VALUE) return null

        var index = (afterIndex + 1).coerceAtLeast(0)
        val now = System.currentTimeMillis()

        while (true) {
            val triggerAt = getTriggerTime(
                todo = todo,
                repeatIndex = index,
                isEarly = isEarly
            ) ?: return null

            if (triggerAt > now) {
                return NextAlarm(
                    repeatIndex = index,
                    triggerAtMillis = triggerAt
                )
            }

            if (index == Int.MAX_VALUE) return null
            index++
        }
    }

    private data class NextAlarm(
        val repeatIndex: Int,
        val triggerAtMillis: Long
    )

    private fun createIntent(
        todoId: Long,
        isEarly: Boolean
    ): Intent {
        val type = if (isEarly) "early" else "due"

        return Intent(appContext, TodoAlarmReceiver::class.java).apply {
            data = "mytodoapp://reminders/$todoId/$type".toUri()
        }
    }
}