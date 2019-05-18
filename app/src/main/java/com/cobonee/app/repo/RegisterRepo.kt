package com.cobonee.app.repo

import android.text.Editable
import com.cobonee.app.R
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.RegisterBody
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall
import java.io.IOException


class RegisterRepo(private val apiService: RetrofitApiService) {

    //=====================================Cities=========================================================

    suspend fun register(
        name: String,
        username: String,
        password: String,
        cityId:String
    ): DataResource<LoginResponse> {
        return safeApiCall(
            call = { registerCall(name,username,password,cityId) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun registerCall(name: String,username: String, password: String,cityId:String): DataResource<LoginResponse> {
        return if (name.isNotBlank() &&username.isNotBlank() && password.isNotBlank()) {
            val response = apiService.getRegisterAsync(RegisterBody(name,username, password,cityId)).await()
            if (response.errors != null) {
                DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                DataResource.Success(response)
            }
        }else{
            DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_register_empty)))
        }
    }
    //=======================================================================================================


}