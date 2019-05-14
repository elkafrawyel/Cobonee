package com.cobonee.app.useCases

import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.ReasonsResponse
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.SettingsRepo
import com.cobonee.app.utily.DataResource

class ReasonsUseCase(private val settingsRepo: SettingsRepo) {

    suspend fun getReasons(): DataResource<ReasonsResponse> {

        return settingsRepo.getReasons()
    }
}