package com.cobonee.app.entity

import com.squareup.moshi.Json

data class CityResponse(
    @field:Json(name = "data")
    val cities: List<City>
)

data class City(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String
){
    override fun toString(): String {
        return name
    }
}
