package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.CreateOrderBody
import com.cobonee.app.entity.CreateOrderResponse
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.entity.OrdersResponse
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall

class OrdersRepo(private val apiService: RetrofitApiService, private val preferencesHelper: PreferencesHelper) {

    //=====================================Orders=========================================================

    suspend fun getOrders(): DataResource<OrdersResponse> {
        return safeApiCall(
            call = { ordersCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private suspend fun ordersCall(): DataResource<OrdersResponse> {
        val response = apiService.getAuthOrdersAsync(
            "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}"
        ).await()
        return DataResource.Success(response)

    }
    //=======================================================================================================

    //=====================================Orders=========================================================

    suspend fun createOrder(idsList: Array<Int>, quantitiesList: Array<Int>): DataResource<CreateOrderResponse> {
        return safeApiCall(
            call = { createOrderCall(idsList, quantitiesList) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_offers)
        )
    }

    private suspend fun createOrderCall(idsList: Array<Int>, quantitiesList: Array<Int>): DataResource<CreateOrderResponse> {
        val response = apiService.createOrderAsync(
            "${Constants.AUTHORIZATION_START} ${preferencesHelper.token}"
            , CreateOrderBody(idsList, quantitiesList)
        ).await()
        return DataResource.Success(response)

    }
    //=======================================================================================================


}