package com.cobonee.app.useCases

import com.cobonee.app.repo.FavouritesRepo
import com.cobonee.app.utily.DataResource

class RemoveFavouritesUseCase (private var favouritesRepo: FavouritesRepo) {
    suspend fun removeOffer(offerId: Int): DataResource<Boolean> {

        return favouritesRepo.remove(offerId)
    }
}