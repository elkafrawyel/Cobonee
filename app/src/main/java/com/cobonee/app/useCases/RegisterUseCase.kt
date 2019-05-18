package com.cobonee.app.useCases

import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.repo.RegisterRepo
import com.cobonee.app.utily.DataResource

class RegisterUseCase(private val registerRepo: RegisterRepo) {

    suspend fun register(
        name: String,
        username: String,
        password: String,
        cityId:String
    ): DataResource<LoginResponse> {

        return registerRepo.register(name,username,password,cityId)
    }
}