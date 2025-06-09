package com.example.todomanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todomanager.data.model.Todo
import com.example.todomanager.data.model.TodoRequest
import com.example.todomanager.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadTodos()
    }

    fun loadTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _todos.value = repository.getAllTodos()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTodo(title: String, description: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val todoRequest = TodoRequest(
                    title = title,
                    description = description,
                    completed = false
                )
                repository.createTodo(todoRequest)
                loadTodos()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to create todo"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleTodoComplete(todo: Todo) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                todo.id?.let { id ->
                    repository.toggleTodoComplete(id)
                    loadTodos()
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update todo"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                todo.id?.let { id ->
                    repository.deleteTodo(id)
                    loadTodos()
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete todo"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
