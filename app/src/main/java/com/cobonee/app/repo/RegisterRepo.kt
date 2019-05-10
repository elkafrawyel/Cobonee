package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.LoginBody
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.RegisterBody
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall
import java.io.IOException


class RegisterRepo(private val apiService: RetrofitApiService) {

    //=====================================Cities=========================================================

    suspend fun getRegister(name: String,username: String, password: String): DataResource<LoginResponse> {
        return safeApiCall(
            call = { registerCall(name,username,password) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun registerCall(name: String,username: String, password: String): DataResource<LoginResponse> {
        if (name.isNotBlank() &&username.isNotBlank() && password.isNotBlank()) {
            val response = apiService.getRegisterAsync(RegisterBody(name,username, password)).await()
            if (response.errors != null) {
                return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                return DataResource.Success(response)
            }
        }else{
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login_empty)))
        }
    }
    //=======================================================================================================


}