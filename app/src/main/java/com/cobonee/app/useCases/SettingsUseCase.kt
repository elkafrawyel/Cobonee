package com.cobonee.app.useCases

import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.Setting
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.LoginRepo
import com.cobonee.app.repo.SettingsRepo
import com.cobonee.app.utily.DataResource

class SettingsUseCase(private val SettingsRepo: SettingsRepo) {

    suspend fun getSettings(): DataResource<Setting> {

        return SettingsRepo.getSettings()
    }
}