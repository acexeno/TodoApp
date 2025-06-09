package com.example.todomanager.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Utility class for handling date and time operations
 */
object DateTimeUtils {
    
    // Standard date formats
    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"
    private const val DISPLAY_TIME_FORMAT = "hh:mm a"
    private const val DISPLAY_DATE_TIME_FORMAT = "MMM dd, yyyy hh:mm a"
    
    // Formatters
    private val serverFormat = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    
    private val displayDateFormat = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault())
    private val displayTimeFormat = SimpleDateFormat(DISPLAY_TIME_FORMAT, Locale.getDefault())
    private val displayDateTimeFormat = SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT, Locale.getDefault())
    
    /**
     * Format date to display string (e.g., "Jun 09, 2023")
     */
    fun formatDate(date: Date?): String {
        if (date == null) return ""
        return displayDateFormat.format(date)
    }
    
    /**
     * Format time to display string (e.g., "02:30 PM")
     */
    fun formatTime(date: Date?): String {
        if (date == null) return ""
        return displayTimeFormat.format(date)
    }
    
    /**
     * Format date and time to display string (e.g., "Jun 09, 2023 02:30 PM")
     */
    fun formatDateTime(date: Date?): String {
        if (date == null) return ""
        return displayDateTimeFormat.format(date)
    }
    
    /**
     * Parse server date string to Date object
     */
    fun parseServerDate(dateString: String?): Date? {
        if (dateString.isNullOrBlank()) return null
        return try {
            serverFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Format date to server date string
     */
    fun formatToServerDate(date: Date?): String {
        if (date == null) return ""
        return serverFormat.format(date)
    }
    
    /**
     * Get relative time span string (e.g., "2 hours ago", "3 days ago")
     */
    fun getRelativeTimeSpanString(date: Date?): String {
        if (date == null) return ""
        
        val now = Date()
        val duration = now.time - date.time
        
        return when {
            duration < TimeUnit.MINUTES.toMillis(1) -> "Just now"
            duration < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            }
            duration < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(duration)
                "$hours ${if (hours == 1L) "hour" else "hours"} ago"
            }
            duration < TimeUnit.DAYS.toMillis(30) -> {
                val days = TimeUnit.MILLISECONDS.toDays(duration)
                "$days ${if (days == 1L) "day" else "days"} ago"
            }
            duration < TimeUnit.DAYS.toMillis(365) -> {
                val months = TimeUnit.MILLISECONDS.toDays(duration) / 30
                "$months ${if (months == 1L) "month" else "months"} ago"
            }
            else -> {
                val years = TimeUnit.MILLISECONDS.toDays(duration) / 365
                "$years ${if (years == 1L) "year" else "years"} ago"
            }
        }
    }
    
    /**
     * Check if two dates are the same day
     */
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
    
    /**
     * Get start of day (00:00:00) for the given date
     */
    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
    
    /**
     * Get end of day (23:59:59.999) for the given date
     */
    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.time
    }
}
