package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.*
import com.cobonee.app.storage.local.PreferencesHelper
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall

class UserRepo(private var preferenceHelper: PreferencesHelper, private var apiService: RetrofitApiService) {

    //=====================================save user=========================================================

    fun saveUser(user: User): DataResource<Boolean> {
        preferenceHelper.id = user.id
        preferenceHelper.name = user.name
        preferenceHelper.email = user.email
        preferenceHelper.cityName = user.city?.name
        preferenceHelper.cityId = user.city?.id!!
        preferenceHelper.mobile = user.mobile
        preferenceHelper.gender = user.gender
        if (user.token != null)
            preferenceHelper.token = user.token
        preferenceHelper.isLoggedIn = true

        return DataResource.Success(true)
    }

    //=======================================================================================================

    //=====================================Update Profile====================================================

    suspend fun updateProfile(updateProfileBody: UpdateProfileBody): DataResource<UpdateProfileResponse> {
        return safeApiCall(
            call = { updateProfileCall(updateProfileBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun updateProfileCall(updateProfileBody: UpdateProfileBody): DataResource<UpdateProfileResponse> {
        val result = apiService.updateProfileAsync(
            "${Constants.AUTHORIZATION_START} ${preferenceHelper.token}", updateProfileBody
        ).await()

        return DataResource.Success(result)
    }

    //========================================================================================================

    //=============================================getUser====================================================

    fun getUser(): User {
        val user = User(
            preferenceHelper.id,
            preferenceHelper.token,
            preferenceHelper.name,
            preferenceHelper.email,
            City(preferenceHelper.cityId, preferenceHelper.cityName),
            preferenceHelper.mobile,
            preferenceHelper.gender
        )

        return user
    }

    //========================================================================================================

    //=====================================contact us Profile====================================================

    suspend fun contactUs(updateProfileBody: ContactUseBody): DataResource<Boolean> {
        return safeApiCall(
            call = { contactUsCall(updateProfileBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun contactUsCall(contactUseBody: ContactUseBody): DataResource<Boolean> {
        val result = apiService.contactUsAsync(contactUseBody).await()
        return DataResource.Success(true)
    }

    //========================================================================================================

}