package com.cobonee.app.entity

import com.squareup.moshi.Json


data class DepartmentResponse(
    @field:Json(name = "data")
    val departments: List<Department>
)

data class Department(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "photos")
    val photos: List<Photo>,
    var isSelected: Boolean = false
)

data class Photo(
    @field:Json(name = "thumb")
    val thumb: String,
    @field:Json(name = "small")
    val small: String,
    @field:Json(name = "medium")
    val medium: String,
    @field:Json(name = "large")
    val large: String,
    @field:Json(name = "original")
    val original: String
)