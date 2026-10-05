package com.example.mytodoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY createdAt ASC, id ASC")
    fun getAllTodos(): Flow<List<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo): Long

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("UPDATE todos SET isCompleted = :isCompleted WHERE id = :todoId")
    suspend fun updateCompleted(
        todoId: Long,
        isCompleted: Boolean
    )

    @Query("UPDATE todos SET title = :title, description = :description WHERE id = :todoId")
    suspend fun updateContent(
        todoId: Long,
        title: String,
        description: String
    )

    @Query("SELECT * FROM todos WHERE id = :todoId LIMIT 1")
    suspend fun getTodoById(todoId: Long): Todo?
}