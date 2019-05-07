package com.cobonee.app.useCases

import com.cobonee.app.entity.CityResponse
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.utily.DataResource

class CitiesUseCase(private val citiesRepo: CitiesRepo) {

    suspend fun getCities(): DataResource<CityResponse> {

        return citiesRepo.getCities()
    }
}