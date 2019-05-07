package com.cobonee.app.useCases

import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.repo.HomeFragmentRepo
import com.cobonee.app.utily.DataResource

class OffersUseCase(private val homeFragmentRepo: HomeFragmentRepo) {

    suspend fun getOffers(department_id: String, city_id: String): DataResource<OffersResponse> {

        return homeFragmentRepo.getOffers(department_id, city_id)
    }
}