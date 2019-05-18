package com.cobonee.app.useCases

import com.cobonee.app.R
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.UpdateProfileBody
import com.cobonee.app.repo.UserRepo
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class UpdateProfileUseCase(private var userRepo: UserRepo) {
    suspend fun update(updateProfileBody: UpdateProfileBody): DataResource<LoginResponse> {

        if (updateProfileBody.name.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_name)))
        if (updateProfileBody.mobile.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_mobile)))

        return userRepo.updateProfile(updateProfileBody)
    }
}