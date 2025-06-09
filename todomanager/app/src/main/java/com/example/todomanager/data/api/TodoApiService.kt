package com.example.todomanager.data.api

import com.example.todomanager.data.model.Todo
import com.example.todomanager.data.model.TodoRequest
import retrofit2.Response
import retrofit2.http.*

interface TodoApiService {
    @GET("todos/")
    suspend fun getTodos(): List<Todo>

    @GET("todos/{id}/")
    suspend fun getTodo(@Path("id") id: Int): Todo

    @POST("todos/")
    suspend fun createTodo(@Body todo: TodoRequest): Todo

    @PUT("todos/{id}/")
    suspend fun updateTodo(@Path("id") id: Int, @Body todo: TodoRequest): Todo

    @DELETE("todos/{id}/")
    suspend fun deleteTodo(@Path("id") id: Int): Response<Unit>

    @POST("todos/{id}/toggle_complete/")
    suspend fun toggleComplete(@Path("id") id: Int): Response<Map<String, Any>>

    @GET("todos/completed/")
    suspend fun getCompletedTodos(): List<Todo>
    
    @GET("todos/pending/")
    suspend fun getPendingTodos(): List<Todo>
}
