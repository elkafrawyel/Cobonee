package com.cobonee.app.entity

import com.squareup.moshi.Json

data class OffersResponse(
    @field:Json(name = "data")
    val offers: List<Offer>,
    @field:Json(name = "links")
    val links: Links,
    @field:Json(name = "meta")
    val meta: Meta
)


data class Offer(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "price")
    val price: String,
    @field:Json(name = "discount")
    val discount: String,
    @field:Json(name = "price_after_discount")
    val priceAfterDiscount: Int
)

data class Links(
    @field:Json(name = "first")
    val first: String?,
    @field:Json(name = "last")
    val last: String?,
    @field:Json(name = "prev")
    val prev: Any?,
    @field:Json(name = "next")
    val next: Any?
)

data class Meta(
    @field:Json(name = "current_page")
    val currentPage: Int?,
    @field:Json(name = "from")
    val from: Any?,
    @field:Json(name = "last_page")
    val lastPage: Int?,
    @field:Json(name = "path")
    val path: String?,
    @field:Json(name = "per_page")
    val perPage: Int?,
    @field:Json(name = "to")
    val to: Any?,
    @field:Json(name = "total")
    val total: Int?
)