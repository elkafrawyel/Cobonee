package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall
import java.io.IOException

class HomeFragmentRepo(private val apiService: RetrofitApiService) {

    //=====================================Offers=========================================================

    suspend fun getOffers(department_id: String, city_id: String): DataResource<OffersResponse> {
        return safeApiCall(
            call = { loginCall(department_id, city_id) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private suspend fun loginCall(department_id: String, city_id: String): DataResource<OffersResponse> {
        val response = apiService.getOffers(department_id, city_id).await()
        return DataResource.Success(response)
    }
    //=======================================================================================================
}