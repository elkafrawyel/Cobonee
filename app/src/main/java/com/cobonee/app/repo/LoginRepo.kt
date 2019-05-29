package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.LoginBody
import com.cobonee.app.entity.LoginFaceBody
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall
import java.io.IOException
import java.lang.Exception


class LoginRepo(private val apiService: RetrofitApiService) {

    //=====================================Login=========================================================

    suspend fun getLogin(username: String, password: String): DataResource<LoginResponse> {
        return safeApiCall(
            call = { loginCall(username,password) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun loginCall(username: String, password: String): DataResource<LoginResponse> {
        if (username.isNotBlank() && password.isNotBlank()) {
            val response = apiService.getLoginAsync(LoginBody(username, password)).await()
            if (response.error != null) {
                return DataResource.Error(IOException(response.error))
//                return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                return DataResource.Success(response)
            }
        }else{
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login_empty)))
        }
    }

    //=====================================login face=========================================================

    suspend fun getFaceLogin(loginFaceBody: LoginFaceBody): DataResource<LoginResponse> {
        return safeApiCall(
            call = { loginFaceCall(loginFaceBody) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun loginFaceCall(loginFaceBody: LoginFaceBody): DataResource<LoginResponse> {
        if (loginFaceBody.access_token.isNotBlank()) {
            val response = apiService.getLoginFaceAsync(loginFaceBody).await()
            if (response.error != null) {
                return DataResource.Error(IOException(response.error))
//                return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                return DataResource.Success(response)
            }
        }else{
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_general)))
        }
    }
    //=======================================================================================================


}