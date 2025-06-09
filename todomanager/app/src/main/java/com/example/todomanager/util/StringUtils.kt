package com.example.todomanager.util

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.TextView
import java.util.*
import java.util.regex.Pattern

/**
 * Utility class for string operations and validations
 */
object StringUtils {
    
    // Email validation pattern
    private const val EMAIL_PATTERN = "[a-zA-Z0-9\+\.\_\%\-\+]{1,256}" +
            "\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}" +
            "(" +
            "\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25}" +
            ")+"
    
    // Password pattern: at least 8 characters, 1 letter, 1 number, 1 special character
    private const val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
    
    /**
     * Check if the string is a valid email address
     */
    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    }
    
    /**
     * Check if the string is a valid password
     */
    fun isValidPassword(password: String?): Boolean {
        if (password.isNullOrBlank()) return false
        return password.length >= 8 && Pattern.matches(".*[A-Za-z].*", password) && 
               Pattern.matches(".*\\d.*", password) && 
               Pattern.matches(".*[!@#$%^&*()_+\-=\\[\]{};':\"\\\\|,.<>/?].*", password)
    }
    
    /**
     * Capitalize the first letter of a string
     */
    fun capitalizeFirstLetter(input: String?): String {
        if (input.isNullOrBlank()) return ""
        return input.substring(0, 1).uppercase(Locale.getDefault()) + 
               input.substring(1).lowercase(Locale.getDefault())
    }
    
    /**
     * Truncate a string with ellipsis if it's longer than maxLength
     */
    fun truncateWithEllipsis(input: String?, maxLength: Int): String {
        if (input.isNullOrBlank()) return ""
        return if (input.length <= maxLength) {
            input
        } else {
            "${input.substring(0, maxLength - 3)}..."
        }
    }
    
    /**
     * Check if the string contains only digits
     */
    fun isNumeric(input: String?): Boolean {
        if (input.isNullOrBlank()) return false
        return input.matches("-?\\d+(\.\\d+)?".toRegex())
    }
    
    /**
     * Remove all non-digit characters from a string
     */
    fun keepOnlyDigits(input: String?): String {
        if (input.isNullOrBlank()) return ""
        return input.replace("[^0-9]".toRegex(), "")
    }
    
    /**
     * Format a string as a phone number (e.g., (123) 456-7890)
     */
    fun formatPhoneNumber(phone: String?): String {
        if (phone.isNullOrBlank()) return ""
        
        val digits = keepOnlyDigits(phone)
        return when (digits.length) {
            10 -> "(${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6)}"
            11 -> "${digits[0]} (${digits.substring(1, 4)}) ${digits.substring(4, 7)}-${digits.substring(7)}"
            else -> phone
        }
    }
    
    /**
     * Make links in a TextView clickable
     */
    fun makeLinksClickable(textView: TextView) {
        textView.movementMethod = LinkMovementMethod.getInstance()
        val text = textView.text.toString()
        val spannableString = SpannableString(text)
        
        // Find URLs in the text and make them clickable
        val urlPattern = Pattern.compile(
            "(?:^|[\\s.:;?\\-\\]\\()" +
            "((?:https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\(\))?)"
        )
        
        val matcher = urlPattern.matcher(text)
        while (matcher.find()) {
            var url = matcher.group(1)
            if (url.startsWith("www.") || url.startsWith("pic.")) {
                url = "http://$url"
            }
            spannableString.setSpan(
                URLSpan(url),
                matcher.start(1),
                matcher.end(1),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        
        textView.text = spannableString
    }
    
    /**
     * Join a list of strings with a separator
     */
    fun joinWithSeparator(
        items: List<String>,
        separator: String = ", "
    ): String {
        return items.filter { it.isNotBlank() }.joinToString(separator)
    }
    
    /**
     * Generate initials from a name
     */
    fun getInitials(name: String?): String {
        if (name.isNullOrBlank()) return ""
        
        val parts = name.trim().split("\\s+".toRegex())
        return when (parts.size) {
            0 -> ""
            1 -> parts[0].take(2).uppercase()
            else -> "${parts[0].first()}${parts.last().first()}".uppercase()
        }
    }
    
    /**
     * Check if the string is a valid URL
     */
    fun isValidUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        return android.util.Patterns.WEB_URL.matcher(url).matches()
    }
    
    /**
     * Remove HTML tags from a string
     */
    fun stripHtml(html: String?): String {
        if (html.isNullOrBlank()) return ""
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            android.text.Html.fromHtml(html, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            @Suppress("DEPRECATION")
            android.text.Html.fromHtml(html).toString()
        }
    }
    
    /**
     * Convert a string to title case
     */
    fun toTitleCase(input: String?): String {
        if (input.isNullOrBlank()) return ""
        
        val sb = StringBuilder()
        var nextTitleCase = true
        
        for (c in input) {
            when {
                c.isWhitespace() -> {
                    nextTitleCase = true
                    sb.append(c)
                }
                nextTitleCase -> {
                    sb.append(c.uppercaseChar())
                    nextTitleCase = false
                }
                else -> sb.append(c.lowercaseChar())
            }
        }
        
        return sb.toString()
    }
}
