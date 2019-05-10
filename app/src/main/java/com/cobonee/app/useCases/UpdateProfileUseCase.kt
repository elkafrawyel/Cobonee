package com.cobonee.app.useCases

import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.UpdateProfileBody
import com.cobonee.app.repo.UserRepo
import com.cobonee.app.utily.DataResource

class UpdateProfileUseCase(private var userRepo: UserRepo) {
    suspend fun update(updateProfileBody: UpdateProfileBody): DataResource<LoginResponse> {
        return userRepo.updateProfile(updateProfileBody)
    }
}