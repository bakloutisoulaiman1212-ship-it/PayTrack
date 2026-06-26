package com.example.paytrack.data.localuser

import android.content.Context

class SessionManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLogin(username: String) {
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putString("username", username)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }

    fun getUsername(): String? {
        return prefs.getString("username", null)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
    fun saveDarkMode(isDark: Boolean) {
        prefs.edit()
            .putBoolean("dark_mode", isDark)
            .apply()
    }

    fun getDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }
}