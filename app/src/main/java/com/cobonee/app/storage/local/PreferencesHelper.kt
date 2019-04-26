package com.cobonee.app.storage.local

import android.content.Context
import android.preference.PreferenceManager

class PreferencesHelper(private val context: Context) {
    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val TOKEN = "token"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var isLoggedIn = preference.getBoolean(IS_LOGGED_IN, false)
        set(value) = preference.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var token = preference.getString(TOKEN, null)
        set(value) = preference.edit().putString(TOKEN, value).apply()
}