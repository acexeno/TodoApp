package com.yourdomain.todomanager.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey
    val id: String = "",
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("completed")
    val completed: Boolean = false,
    
    @SerializedName("priority")
    val priority: String = "medium", // low, medium, high
    
    @SerializedName("due_date")
    val dueDate: Date? = null,
    
    @SerializedName("created_at")
    val createdAt: Date = Date(),
    
    @SerializedName("updated_at")
    val updatedAt: Date = Date()
) 