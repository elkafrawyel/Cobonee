package com.cobonee.app.entity

import com.squareup.moshi.Json

data class User(
    var id: Int,
    var token: String,
    var name: String,
    var email: String?,
    var city: City?,
    var mobile: String?,
    var gender: String?
)


data class UpdateProfileBody(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "city_id") val cityId: Int,
    @field:Json(name = "mobile") val mobile: String,
    @field:Json(name = "gender") val gender: String
)

data class ContactUseBody(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "department_id") val reason: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "message") val message: String,
    @field:Json(name = "mobile") val mobile: String
)
data class ContactUseResponse(
    @field:Json(name = "message") val message: String
)