package com.fred.swyppy.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("SwypyPrefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        const val KEY_USER_ID = "userId"
        const val KEY_USER_EMAIL = "userEmail"
        const val KEY_USER_NAME = "userName"
        const val KEY_USER_ROLE = "userRole"
    }

    // Save login session
    fun saveLoginSession(
        userId: String,
        email: String,
        name: String,
        role: String
    ) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_ROLE, role)
            apply()
        }
    }

    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Get user role
    fun getUserRole(): String {
        return prefs.getString(KEY_USER_ROLE, "user") ?: "user"
    }

    // Get user ID
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    // Get user email
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    // Get user name
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    // Clear session (logout)
    fun clearSession() {
        prefs.edit().clear().apply()
    }
}