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
    @field:Json(name = "quetions")
    val quetions: List<Quetion>,
    @field:Json(name = "common_quetion_1")
    val common_quetion_1: CommonQuetion1,
    @field:Json(name = "common_quetion_2")
    val common_quetion_2: CommonQuetion2,
    @field:Json(name = "common_quetion_3")
    val common_quetion_3: CommonQuetion3,
    @field:Json(name = "common_quetion_4")
    val common_quetion_4: CommonQuetion4
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

data class CommonQuetion1(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)


data class CommonQuetion3(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)

data class CommonQuetion2(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)
data class CommonQuetion4(
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "title")
    val title: String
)

fun CommonQuetion1.toQuetion(q: CommonQuetion1): Quetion {
    return Quetion(q.content, q.id, q.title)
}

fun CommonQuetion2.toQuetion(q: CommonQuetion2): Quetion {
    return Quetion(q.content, q.id, q.title)
}

fun CommonQuetion3.toQuetion(q: CommonQuetion3): Quetion {
    return Quetion(q.content, q.id, q.title)
}

fun CommonQuetion4.toQuetion(q: CommonQuetion4): Quetion {
    return Quetion(q.content, q.id, q.title)
}