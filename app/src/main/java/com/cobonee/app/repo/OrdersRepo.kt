package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall

class OrdersRepo(private val apiService: RetrofitApiService, private val preferencesHelper: PreferencesHelper) {

    //=====================================Orders=========================================================

    suspend fun getOrders(): DataResource<OffersResponse> {
        return safeApiCall(
            call = { ordersCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private suspend fun ordersCall(): DataResource<OffersResponse> {
            val response = apiService.getAuthOrdersAsync(
                "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}"
            ).await()
            return DataResource.Success(response)

    }
    //=======================================================================================================
}