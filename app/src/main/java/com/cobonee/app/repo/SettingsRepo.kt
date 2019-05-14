package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.ReasonsResponse
import com.cobonee.app.entity.Setting
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall


class SettingsRepo(private val apiService: RetrofitApiService) {

    //=====================================Settings=========================================================

    suspend fun getSettings(): DataResource<Setting> {
        return safeApiCall(
            call = { settingsCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun settingsCall(): DataResource<Setting> {
        val response = apiService.getSettings().await()
        return DataResource.Success(response)
    }
    //=======================================================================================================

    //=====================================Reasons=========================================================

    suspend fun getReasons(): DataResource<ReasonsResponse> {
        return safeApiCall(
            call = { reasonsCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun reasonsCall(): DataResource<ReasonsResponse> {
        val response = apiService.getReasonsAsync().await()
        return DataResource.Success(response)
    }
    //=======================================================================================================


}