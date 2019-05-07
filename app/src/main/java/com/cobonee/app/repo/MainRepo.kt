package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.CitiesResponse
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall


class MainRepo(private val apiService: RetrofitApiService) {

    //=====================================Cities=========================================================

    suspend fun getCities(): DataResource<CitiesResponse> {
        return safeApiCall(
            call = { citiesCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun citiesCall(): DataResource<CitiesResponse> {
        val response = apiService.getCities().await()
        return DataResource.Success(response)
    }
    //=======================================================================================================
}