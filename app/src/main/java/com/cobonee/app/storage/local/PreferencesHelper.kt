package com.cobonee.app.storage.local

import android.content.Context
import android.preference.PreferenceManager
import com.cobonee.app.utily.Constants

class PreferencesHelper(private val context: Context) {
    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val TOKEN = "token"
        private const val LANGUAGE = "language"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var isLoggedIn = preference.getBoolean(IS_LOGGED_IN, false)
        set(value) = preference.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var token = preference.getString(TOKEN, null)
        set(value) = preference.edit().putString(TOKEN, value).apply()

    var language = preference.getString(LANGUAGE, Constants.Language.ARABIC.value)
        set(value) = preference.edit().putString(LANGUAGE, value).apply()
}