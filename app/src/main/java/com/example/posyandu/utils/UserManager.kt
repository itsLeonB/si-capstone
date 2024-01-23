package com.example.posyandu.utils

import android.content.Context
import com.example.posyandu.features.authentication.LoginActivity
import com.example.posyandu.features.main.home.HomeViewModel

class UserManager(private val context: Context) {

    companion object {
        private const val PREFERENCES_NAME = "UserPreferences"
        private const val KEY_TOKEN = "token"
        private const val KEY_ROLE = "role"

        @Volatile
        private var instance: UserManager? = null

        fun getInstance(context: LoginActivity): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager(context).also { instance = it }
            }
        }
    }

    fun saveUserDetails(token: String, role: String) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(KEY_TOKEN, token)
            putString(KEY_ROLE, role)
            apply()
        }
    }

    fun getToken(): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun getRole(): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_ROLE, null)
    }
}
