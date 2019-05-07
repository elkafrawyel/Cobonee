package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.CityResponse
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall


class CitiesRepo(private val apiService: RetrofitApiService) {

    //=====================================Cities=========================================================

    suspend fun getCities(): DataResource<CityResponse> {
        return safeApiCall(
            call = { citiesCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun citiesCall(): DataResource<CityResponse> {
        val response = apiService.getCitiesAsync().await()
        return DataResource.Success(response)
    }
    //=======================================================================================================


}