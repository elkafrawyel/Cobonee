package com.cobonee.app.useCases

import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.ContactUseBody
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.repo.CitiesRepo
import com.cobonee.app.repo.LoginRepo
import com.cobonee.app.repo.UserRepo
import com.cobonee.app.utily.DataResource

class ContactUsUseCase(private val userRepo: UserRepo) {

    suspend fun getContactUs(updateProfileBody: ContactUseBody): DataResource<String> {

        return userRepo.contactUs(updateProfileBody)
    }
}