package com.cobonee.app.useCases

import com.cobonee.app.repo.FavouritesRepo
import com.cobonee.app.utily.DataResource

class MakeOfferFavouritesUseCase(private var favouritesRepo: FavouritesRepo) {
    suspend fun addOffer(offerId: Int): DataResource<Boolean> {

        return favouritesRepo.add(offerId)
    }
}