package com.xby.shop624.util

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveLogin(phone: String) {
        prefs.edit().putString(KEY_PHONE, phone).apply()
    }

    fun getLoggedInPhone(): String? = prefs.getString(KEY_PHONE, null)

    fun isLoggedIn(): Boolean = !getLoggedInPhone().isNullOrBlank()

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "shop624_session"
        private const val KEY_PHONE = "logged_in_phone"
    }
}
