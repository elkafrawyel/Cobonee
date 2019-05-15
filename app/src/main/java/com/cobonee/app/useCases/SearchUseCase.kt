package com.cobonee.app.useCases

import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.repo.OffersRepo
import com.cobonee.app.utily.DataResource

class SearchUseCase(private val offersRepo: OffersRepo) {

    suspend fun search(query: String, page: Int): DataResource<OffersResponse> {
        return offersRepo.searchOffers(query = query, page = page)
    }
}