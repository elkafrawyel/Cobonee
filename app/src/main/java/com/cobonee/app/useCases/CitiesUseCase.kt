package com.cobonee.app.useCases

import com.cobonee.app.entity.CitiesResponse
import com.cobonee.app.repo.MainRepo
import com.cobonee.app.utily.DataResource

class CitiesUseCase(private val mainRepo: MainRepo) {

    suspend fun getCities(): DataResource<CitiesResponse> {

        return mainRepo.getCities()
    }
}