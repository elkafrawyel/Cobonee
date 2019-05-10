package com.cobonee.app.storage.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.cobonee.app.utily.Constants

class PreferencesHelper(private val context: Context) {
    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val TOKEN = "token"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val CITY = "city"
        private const val MOBILE = "mobile"
        private const val GENDER = "gender"
        private const val LANGUAGE = "language"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var isLoggedIn = preference.getBoolean(IS_LOGGED_IN, false)
        set(value) = preference.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var token = preference.getString(TOKEN, null)
        set(value) = preference.edit().putString(TOKEN, value).apply()

    var id = preference.getInt(ID, -1)
        set(value) = preference.edit().putInt(ID, value).apply()

    var name = preference.getString(NAME, null)
        set(value) = preference.edit().putString(NAME, value).apply()

    var email = preference.getString(EMAIL, null)
        set(value) = preference.edit().putString(EMAIL, value).apply()

    var city = preference.getString(CITY, null)
        set(value) = preference.edit().putString(CITY, value).apply()

    var mobile = preference.getString(MOBILE, null)
        set(value) = preference.edit().putString(MOBILE, value).apply()

    var gender = preference.getString(GENDER, null)
        set(value) = preference.edit().putString(GENDER, value).apply()


    var language = preference.getString(LANGUAGE, Constants.Language.ARABIC.value)
        set(value) = preference.edit().putString(LANGUAGE, value).apply()

    fun clear(){
        var lang = language
        preference.edit().clear().apply()
        language = lang
    }
}