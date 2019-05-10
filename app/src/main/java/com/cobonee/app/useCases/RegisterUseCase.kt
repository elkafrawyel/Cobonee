package com.cobonee.app.useCases

import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.repo.RegisterRepo
import com.cobonee.app.utily.DataResource

class RegisterUseCase(private val registerRepo: RegisterRepo) {

    suspend fun getRegister(name:String,username: String, password: String): DataResource<LoginResponse> {

        return registerRepo.getRegister(name,username,password)
    }
}