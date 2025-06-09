package com.yourdomain.todomanager.ui.todos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yourdomain.todomanager.data.db.TodoDatabase
import com.yourdomain.todomanager.data.models.Todo
import com.yourdomain.todomanager.data.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository
    val todos: Flow<List<Todo>>

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val database = TodoDatabase.getDatabase(application)
        repository = TodoRepository(database.todoDao())
        todos = repository.getAllTodos()
    }

    fun refreshTodos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.refreshTodos()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun getTodo(id: String): Todo? {
        return try {
            repository.getTodo(id)
        } catch (e: Exception) {
            _error.value = e.message
            null
        }
    }

    suspend fun createTodo(todo: Todo): Result<Todo> {
        return try {
            repository.createTodo(todo)
        } catch (e: Exception) {
            _error.value = e.message
            Result.failure(e)
        }
    }

    suspend fun updateTodo(todo: Todo): Result<Todo> {
        return try {
            repository.updateTodo(todo)
        } catch (e: Exception) {
            _error.value = e.message
            Result.failure(e)
        }
    }

    suspend fun deleteTodo(todo: Todo): Result<Unit> {
        return try {
            repository.deleteTodo(todo)
        } catch (e: Exception) {
            _error.value = e.message
            Result.failure(e)
        }
    }

    fun clearError() {
        _error.value = null
    }
} 