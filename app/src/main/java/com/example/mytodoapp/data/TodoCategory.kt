package com.example.mytodoapp.data

enum class TodoCategory(
    val displayName: String
) {
    EVENT("Sự kiện"),
    PERSONAL("Cá nhân"),
    WORK("Công việc"),
    FAVORITES("Yêu thích")
}