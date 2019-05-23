package com.cobonee.app.repo

import com.cobonee.app.R
import com.cobonee.app.entity.*
import com.cobonee.app.storage.remote.RetrofitApiService
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.safeApiCall
import java.io.IOException


class ForgetRepo(private val apiService: RetrofitApiService) {

    //=====================================Send Email=========================================================

    suspend fun getForget(username: String): DataResource<ForgetResponse> {
        return safeApiCall(
            call = { forgetCall(username) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun forgetCall(username: String): DataResource<ForgetResponse> {
        if (username.isNotBlank()) {
            val response = apiService.getForgetAsync(ForgetBody(username)).await()
            if (response.check_the_code != null) {
                return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                return DataResource.Success(response)
            }
        }else{
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_email_empty)))
        }
    }
    //=======================================================================================================

    //=====================================check-code=========================================================

    suspend fun getCheck(username: String, code: String): DataResource<CheckCodeResponse> {
        return safeApiCall(
            call = { checkCall(username, code) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun checkCall(username: String, code: String): DataResource<CheckCodeResponse> {
        if (code.isNotBlank()) {
            val response = apiService.getCheckCodeAsync(CheckCodeBody(username,code)).await()
            if (response.token != null) {
                return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                return DataResource.Success(response)
            }
        }else{
            return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_code_empty)))
        }
    }
    //=======================================================================================================

    //=====================================reset =========================================================

    suspend fun getReset(token: String, password: String, password_confirmation: String): DataResource<LoginResponse> {
        return safeApiCall(
            call = { resetCall(token,password,password_confirmation) },
            errorMessage = Injector.getApplicationContext().getString(R.string.error_general)
        )
    }

    private suspend fun resetCall(token: String, password: String, password_confirmation: String): DataResource<LoginResponse> {
            val response = apiService.getResetAsync(ResetBody(token,password,password_confirmation)).await()
            if (response.data != null) {
                return DataResource.Error(IOException(Injector.getApplicationContext().getString(R.string.error_login)))
            } else {
                return DataResource.Success(response)
            }

    }
    //=======================================================================================================


}