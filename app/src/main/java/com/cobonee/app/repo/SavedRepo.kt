package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.*
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall
import io.objectbox.BoxStore

class SavedRepo(private val box: BoxStore) {

    //=====================================Save =========================================================

    suspend fun save(offer: Offer): DataResource<Boolean> {
        return safeApiCall(
            call = { saveProcess(offer) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private fun saveProcess(offer: Offer): DataResource<Boolean> {
//        val offerBox = box.boxFor(SavedOffer::class.java)
//        val cityBox = box.boxFor(SavedOfferCity::class.java)
//        val photoBox = box.boxFor(SavedOfferPhoto::class.java)
//        val coboneeBox = box.boxFor(SavedCoubone::class.java)
//
//        val savedCity = offer.city?.toSaveOfferCity(offer.city)!!
//        val savedPhoto = offer.photos!![0]!!.toSavedPhoto(offer.photos[0]!!)
//        var savedCobonee: SavedCoubone? = null
//        if (offer.coubones?.size!! > 0) {
//            savedCobonee = offer.coubones.get(0) as SavedCoubone
//        }
//
//        val savedOffer = SavedOffer(
//            0,
//            id = offer.id!!,
//            offerHeader = offer.offerHeader!!,
//            price = offer.price!!,
//            offerBody = offer.offerBody!!,
//            features = offer.features!!,
//            ownerName = offer.ownerName!!,
//            ownerPhone = offer.ownerPhone!!,
//            address = offer.address!!,
//            facebook = offer.facebook!!,
//            twitter = offer.twitter!!,
//            instagram = offer.instagram!!,
//            discount = offer.discount!!,
//            priceAfterDiscount = offer.priceAfterDiscount!!
//        )
//
//        savedCity.savedOffer.add(savedOffer)
//        cityBox.put(savedCity)
//        savedOffer.savedCity.add(savedCity)
//
//        savedPhoto.savedOffer.add(savedOffer)
//        photoBox.put(savedPhoto)
//        savedOffer.savedPhoto.add(savedPhoto)
//
//        if (savedCobonee != null) {
//            savedCobonee.savedOffer.add(savedOffer)
//            coboneeBox.put(savedCobonee)
//            savedOffer.savedCobonee.add(savedCobonee)
//        }
//
//        val key = offerBox.put(savedOffer)
//
//
////        var savedOffer = offerBox.all
        return DataResource.Success(true)
    }
    //=======================================================================================================

}