package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.*
import java.io.IOException

class FavouritesRepo(private var apiService: RetrofitApiService, private var preferenceHelper: PreferencesHelper) {

    suspend fun get(): DataResource<List<Offer>> {
        return safeApiCall(
            call = { getCall() },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun getCall(): DataResource<List<Offer>> {

        val response = apiService.favouritesAsync(
            "${Constants.AUTHORIZATION_START} ${preferenceHelper.token}"
        ).await()
        return if (response.offers.isNotEmpty()) {
            DataResource.Success(response.offers)
        } else {
            DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.no_favourites)))
        }

    }

    suspend fun add(offerId: Int): DataResource<Boolean> {
        return safeApiCall(
            call = { getAdd(offerId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun getAdd(offerId: Int): DataResource<Boolean> {

        if (preferenceHelper.isLoggedIn) {
            val response = apiService.makeFavouritesAsync(
                "${Constants.AUTHORIZATION_START} ${preferenceHelper.token}", offerId
            ).await()
            return if (response.message == Constants.SUCCESS) {
                return DataResource.Success(true)
            } else {
                DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_general)))
            }
        } else {
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_you_must_login)))
        }
    }

    suspend fun remove(offerId: Int): DataResource<Boolean> {
        return safeApiCall(
            call = { getRemove(offerId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun getRemove(offerId: Int): DataResource<Boolean> {
        if (preferenceHelper.isLoggedIn) {

            val response = apiService.removeFavouritesAsync(
                "${Constants.AUTHORIZATION_START} ${preferenceHelper.token}", offerId
            ).await()
            return if (response.message == Constants.SUCCESS) {
                return DataResource.Success(true)
            } else {
                DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_general)))
            }
        } else {
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_you_must_login)))
        }
    }
}