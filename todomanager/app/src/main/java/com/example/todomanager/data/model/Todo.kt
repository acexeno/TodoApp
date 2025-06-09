package com.example.todomanager.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Todo(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("completed") val completed: Boolean = false,
    @SerializedName("priority") val priority: String = "medium",
    @SerializedName("due_date") val dueDate: Date? = null,
    @SerializedName("created_date") val createdDate: Date? = null
) {
    val isOverdue: Boolean
        get() = !completed && dueDate?.before(Date()) == true
}

data class TodoRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("completed") val completed: Boolean = false,
    @SerializedName("priority") val priority: String = "medium",
    @SerializedName("due_date") val dueDate: Date? = null
)
