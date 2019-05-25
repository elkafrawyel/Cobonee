package com.cobonee.app.useCases

import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.LoginFaceBody
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.LoginRepo
import com.cobonee.app.utily.DataResource

class LoginUseCase(private val loginRepo: LoginRepo) {

    suspend fun getLogin(username: String, password: String): DataResource<LoginResponse> {

        return loginRepo.getLogin(username,password)
    }

    suspend fun getFaceLogin(loginFaceBody: LoginFaceBody): DataResource<LoginResponse> {

        return loginRepo.getFaceLogin(loginFaceBody)
    }
}