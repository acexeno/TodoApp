package com.yourdomain.todomanager.data.api

import com.yourdomain.todomanager.data.models.Todo
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/todos/")
    suspend fun getTodos(): Response<List<Todo>>
    
    @GET("api/todos/{id}/")
    suspend fun getTodo(@Path("id") id: String): Response<Todo>
    
    @POST("api/todos/")
    suspend fun createTodo(@Body todo: Todo): Response<Todo>
    
    @PUT("api/todos/{id}/")
    suspend fun updateTodo(@Path("id") id: String, @Body todo: Todo): Response<Todo>
    
    @DELETE("api/todos/{id}/")
    suspend fun deleteTodo(@Path("id") id: String): Response<Unit>
    
    @POST("api/token/")
    suspend fun login(@Body credentials: Map<String, String>): Response<Map<String, String>>
    
    @POST("api/token/refresh/")
    suspend fun refreshToken(@Body refreshToken: Map<String, String>): Response<Map<String, String>>
} 