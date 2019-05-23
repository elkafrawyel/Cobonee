package com.cobonee.app.entity

import com.squareup.moshi.Json

data class LoginBody(
    var email: String,
    var password: String
)

data class RegisterBody(
    var name: String,
    var email: String,
    var password: String,
    val city_id: String
)

data class LoginResponse(
    @field:Json(name = "data")
    val data: Data,
    @field:Json(name = "token")
    val token: String?,
    @field:Json(name = "errors")
    val errors: Errors,
    @field:Json(name = "message")
    val message: String?
)

data class Data(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "email")
    val email: String?,
    @field:Json(name = "cityName")
    val city: City? = City(),
    @field:Json(name = "mobile")
    val mobile: String?,
    @field:Json(name = "gender")
    val gender: String?
)

data class Errors(
    @field:Json(name = "email")
    val email: String?
)

data class ForgetBody(
    var email: String
)

data class ForgetResponse(
    @field:Json(name = "check-the-code") val check_the_code: String
)

data class CheckCodeBody(
    var email: String,
    var code: String
)

data class CheckCodeResponse(
    @field:Json(name = "token") val token: String
)

data class ResetBody(
    var token: String,
    var password: String,
    var password_confirmation: String
)


