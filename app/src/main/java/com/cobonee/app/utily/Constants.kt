package com.cobonee.app.utily

object Constants {
    const val BASE_URL = "http://cobonee.shobeek-lobeek.com/api/"
    const val AUTHORIZATION_START = "Bearer"
    const val SUCCESS = "success"

    enum class Language(val value: String) {
        ARABIC("ar"),
        ENGLISH("en")
    }
}