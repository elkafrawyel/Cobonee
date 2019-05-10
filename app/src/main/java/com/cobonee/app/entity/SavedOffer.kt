package com.cobonee.app.entity

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
data class SavedOffer(
    @Id
    var key: Long = 0,
    val id: Int = 0,
    val offerHeader: String = "",
    val offerBody: String = "",
    val price: String = "",
    val features: String = "",
    val ownerName: String = "",
    val ownerPhone: String = "",
    val address: String = "",
    val facebook: String = "",
    val twitter: String = "",
    val instagram: String = "",
    val discount: String = "",
    val priceAfterDiscount: Int = 0
) {

    @Backlink
    lateinit var savedPhoto: ToMany<SavedOfferPhoto>

    @Backlink
    lateinit var savedCobonee: ToMany<SavedCoubone>

    @Backlink
    lateinit var savedCity: ToMany<SavedOfferCity>
}

@Entity
data class SavedOfferCity(
    @Id
    var key: Long = 0,
    val id: Int = 0,
    val name: String = ""
) {
    lateinit var savedOffer: ToMany<SavedOffer>
}

@Entity
data class SavedOfferPhoto(
    @Id
    var key: Long = 0,
    val thumb: String = "",
    val small: String = "",
    val medium: String = "",
    val large: String = "",
    val original: String? = ""
){
    lateinit var savedOffer: ToMany<SavedOffer>
}

@Entity
data class SavedCoubone(
    @Id
    var key: Long = 0,
    val name: String = ""
){
    lateinit var savedOffer: ToMany<SavedOffer>
}


fun OfferCity.toSaveOfferCity(city: OfferCity): SavedOfferCity {
    return SavedOfferCity(0, city.id, city.name!!)
}

fun OfferPhoto.toSavedPhoto(offerPhoto: OfferPhoto): SavedOfferPhoto {
    return SavedOfferPhoto(
        0,
        offerPhoto.thumb!!,
        offerPhoto.small!!,
        offerPhoto.medium!!,
        offerPhoto.large!!,
        offerPhoto.original!!
    )
}