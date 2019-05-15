package com.cobonee.app.entity

import com.squareup.moshi.Json

data class Setting(
    @field:Json(name = "email")
    val email: String,
    @field:Json(name = "mobile")
    val mobile: String,
    @field:Json(name = "about_us")
    val about_us: AboutUs,
    @field:Json(name = "commession")
    val commession: String,
    @field:Json(name = "common_question")
    val quetions: List<Quetion>
)

data class AboutUs(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)

data class Quetion(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)

data class ReasonsResponse(
    @field:Json(name = "data")
    val reasons: List<Reason>
)

data class Reason(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String
){
    override fun toString(): String {
        return name
    }
}
