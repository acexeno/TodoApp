package com.example.todomanager.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy HH:mm"
    
    private val serverFormat = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    private val displayFormat = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
    
    fun formatDateForDisplay(date: Date): String {
        return displayFormat.format(date)
    }
    
    fun parseServerDate(dateString: String?): Date? {
        if (dateString.isNullOrBlank()) return null
        return try {
            serverFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    fun formatDateForServer(date: Date): String {
        return serverFormat.format(date)
    }
}
