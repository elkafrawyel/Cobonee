package com.cobonee.app.useCases

import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.repo.OffersRepo
import com.cobonee.app.utily.DataResource

class OffersUseCase(private val offersRepo: OffersRepo) {

    suspend fun getOffers(department_id: String, city_id: String): DataResource<OffersResponse> {

        return offersRepo.getOffers(department_id, city_id)
    }
}