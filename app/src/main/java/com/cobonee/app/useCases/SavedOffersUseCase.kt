package com.cobonee.app.useCases

import com.cobonee.app.entity.*
import com.cobonee.app.repo.SavedRepo
import com.cobonee.app.utily.DataResource

class SavedOffersUseCase(private val savedRepo: SavedRepo) {

    suspend fun save(offer: Offer): DataResource<Boolean> {


        return savedRepo.save(offer = offer)
    }
}