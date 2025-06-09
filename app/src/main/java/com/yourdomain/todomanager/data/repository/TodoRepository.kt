package com.yourdomain.todomanager.data.repository

import com.yourdomain.todomanager.data.api.ApiService
import com.yourdomain.todomanager.data.db.TodoDao
import com.yourdomain.todomanager.data.models.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class TodoRepository(
    private val apiService: ApiService,
    private val todoDao: TodoDao
) {
    fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()
    
    suspend fun refreshTodos() {
        try {
            val response = apiService.getTodos()
            if (response.isSuccessful) {
                response.body()?.let { todos ->
                    todoDao.deleteAllTodos()
                    todoDao.insertTodos(todos)
                }
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
    
    suspend fun getTodo(id: String): Todo? {
        return try {
            val response = apiService.getTodo(id)
            if (response.isSuccessful) {
                response.body()?.let { todo ->
                    todoDao.insertTodo(todo)
                    todo
                }
            } else {
                todoDao.getTodoById(id)
            }
        } catch (e: Exception) {
            todoDao.getTodoById(id)
        }
    }
    
    suspend fun createTodo(todo: Todo): Result<Todo> {
        return try {
            val response = apiService.createTodo(todo)
            if (response.isSuccessful) {
                response.body()?.let {
                    todoDao.insertTodo(it)
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to create todo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateTodo(todo: Todo): Result<Todo> {
        return try {
            val response = apiService.updateTodo(todo.id, todo)
            if (response.isSuccessful) {
                response.body()?.let {
                    todoDao.updateTodo(it)
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to update todo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteTodo(todo: Todo): Result<Unit> {
        return try {
            val response = apiService.deleteTodo(todo.id)
            if (response.isSuccessful) {
                todoDao.deleteTodo(todo)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete todo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 