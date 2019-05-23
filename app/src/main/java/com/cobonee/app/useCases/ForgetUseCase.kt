package com.cobonee.app.useCases

import com.cobonee.app.R
import com.cobonee.app.entity.*
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.ForgetRepo
import com.cobonee.app.repo.LoginRepo
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class ForgetUseCase(private val loginRepo: ForgetRepo) {

    suspend fun getForget(username: String): DataResource<ForgetResponse> {

        return loginRepo.getForget(username)
    }

    suspend fun getCheck(username: String, code:String): DataResource<CheckCodeResponse> {

        return loginRepo.getCheck(username,code)
    }

    suspend fun getReset(token: String, password: String, password_confirmation: String): DataResource<LoginResponse> {
        if (password.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.error_pass_empty)))
        if (!password.equals(password_confirmation))
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.error_pass_match)))
        return loginRepo.getReset(token,password,password_confirmation)
    }
}