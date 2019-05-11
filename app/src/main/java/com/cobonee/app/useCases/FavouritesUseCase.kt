package com.cobonee.app.useCases

import com.cobonee.app.entity.Offer
import com.cobonee.app.repo.FavouritesRepo
import com.cobonee.app.utily.DataResource

class FavouritesUseCase(private var favouritesRepo: FavouritesRepo) {
    suspend fun getFavourites(): DataResource<List<Offer>> {
        return favouritesRepo.get()
    }
}