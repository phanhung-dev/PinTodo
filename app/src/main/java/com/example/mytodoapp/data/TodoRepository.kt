package com.example.mytodoapp.data

import kotlinx.coroutines.flow.Flow

class TodoRepository(
    private val todoDao: TodoDao
) {

    fun getAllTodos(): Flow<List<Todo>>{
        return todoDao.getAllTodos()
    }

    suspend fun addTodo(todo: Todo): Long {
        return todoDao.insertTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo){
        todoDao.deleteTodo(todo)
    }

    suspend fun updateCompleted(
        todoId: Long,
        isCompleted: Boolean
    ){
        todoDao.updateCompleted(todoId, isCompleted)
    }

    suspend fun editTodo(
        todoId: Long,
        title: String,
        description: String
    ){
        todoDao.updateContent(todoId, title, description)
    }
}