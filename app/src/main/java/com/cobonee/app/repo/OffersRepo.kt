package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall

class OffersRepo(private val apiService: RetrofitApiService, private val preferencesHelper: PreferencesHelper) {

    //=====================================Offers=========================================================

    suspend fun getOffers(department_id: String, city_id: String, page: Int): DataResource<OffersResponse> {
        return safeApiCall(
            call = { loginCall(department_id, city_id, page) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private suspend fun loginCall(department_id: String, city_id: String, page: Int): DataResource<OffersResponse> {
        if (preferencesHelper.isLoggedIn) {
            val response = apiService.getAuthOffersAsync(
                "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}",
                department_id,
                city_id,
                page.toString()
            ).await()
            return DataResource.Success(response)
        } else {
            val response = apiService.getOffersAsync(department_id, city_id, page.toString()).await()
            return DataResource.Success(response)
        }
    }
    //=======================================================================================================

}