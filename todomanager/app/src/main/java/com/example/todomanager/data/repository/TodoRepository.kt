package com.example.todomanager.data.repository

import com.example.todomanager.data.api.RetrofitClient
import com.example.todomanager.data.api.TodoApiService
import com.example.todomanager.data.model.Todo
import com.example.todomanager.data.model.TodoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val apiService: TodoApiService = RetrofitClient.todoApiService
) {
    suspend fun getAllTodos(): List<Todo> {
        return apiService.getTodos()
    }

    suspend fun getTodoById(id: Int): Todo {
        return apiService.getTodo(id)
    }

    suspend fun createTodo(todo: TodoRequest): Todo {
        return apiService.createTodo(todo)
    }

    suspend fun updateTodo(id: Int, todo: TodoRequest): Todo {
        return apiService.updateTodo(id, todo)
    }

    suspend fun deleteTodo(id: Int): Boolean {
        val response = apiService.deleteTodo(id)
        return response.isSuccessful
    }

    suspend fun toggleTodoComplete(id: Int): Boolean {
        val response = apiService.toggleComplete(id)
        return response.isSuccessful
    }

    fun getCompletedTodos(): Flow<List<Todo>> = flow {
        emit(apiService.getCompletedTodos())
    }.flowOn(Dispatchers.IO)

    fun getPendingTodos(): Flow<List<Todo>> = flow {
        emit(apiService.getPendingTodos())
    }.flowOn(Dispatchers.IO)
}
