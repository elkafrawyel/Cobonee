package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.UpdateProfileBody
import com.cobonee.app.entity.User
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
        preferenceHelper.city = user.city
        preferenceHelper.mobile = user.mobile
        preferenceHelper.gender = user.gender
        if (user.token != null)
            preferenceHelper.token = user.token
        preferenceHelper.isLoggedIn = true

        return DataResource.Success(true)
    }

    //=======================================================================================================

    //=====================================Update Profile====================================================

    suspend fun updateProfile(updateProfileBody: UpdateProfileBody): DataResource<LoginResponse> {
        return safeApiCall(
            call = { updateProfileCall(updateProfileBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun updateProfileCall(updateProfileBody: UpdateProfileBody): DataResource<LoginResponse> {
        val result = apiService.updateProfileAsync(
            "${Constants.AUTHORIZATION_START} ${preferenceHelper.token}", updateProfileBody
        ).await()

        return DataResource.Success(result.copy(token = preferenceHelper.token))
    }

    //========================================================================================================

    //=============================================getUser====================================================

    fun getUser(): User {
        val user = User(
            preferenceHelper.id,
            preferenceHelper.token,
            preferenceHelper.name,
            preferenceHelper.email,
            preferenceHelper.city,
            preferenceHelper.mobile,
            preferenceHelper.gender
        )

        return user
    }

    //========================================================================================================

}