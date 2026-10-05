package com.example.mytodoapp.data

enum class TodoReminder(
    val displayName: String,
    val minutesBefore: Int?
) {
    NONE("Không có", null),
    AT_TIME("Đúng giờ", 0),
    BEFORE_15_MINUTES("Trước 15 phút", 15),
    BEFORE_30_MINUTES("Trước 30 phút", 30),
    BEFORE_1_HOUR("Trước 1 tiếng", 60),
    BEFORE_1_DAY("Trước 1 ngày", 1440)
}