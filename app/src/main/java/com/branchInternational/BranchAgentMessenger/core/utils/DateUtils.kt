package com.branchInternational.BranchAgentMessenger.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    // Input format from API: "2017-02-01T14:51:36.000Z"
    private val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC") // API is in UTC
    }

    // Output format 1: "Jan 31, 2017" (For old dates)
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // Output format 2: "2:51 PM" (For message times)
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    fun formatMessageTime(timestamp: String): String {
        return try {
            val date = apiFormat.parse(timestamp) ?: return timestamp

            // Logic: If the message is from today, show Time. Otherwise, show Date.
            // For this assignment, since data is from 2017, we mostly default to Date format.
            dateFormat.format(date)
        } catch (e: Exception) {
            timestamp // Fallback to raw string if parsing fails
        }
    }

    // Optional: Only shows time (e.g., for the chat bubble specific view)
    fun formatTimeOnly(timestamp: String): String {
        return try {
            val date = apiFormat.parse(timestamp) ?: return ""
            timeFormat.format(date)
        } catch (e: Exception) {
            ""
        }
    }
}