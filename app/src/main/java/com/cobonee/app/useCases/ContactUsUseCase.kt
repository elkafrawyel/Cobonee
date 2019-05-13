package com.cobonee.app.useCases

import com.cobonee.app.R
import com.cobonee.app.entity.ContactUseBody
import com.cobonee.app.repo.UserRepo
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import java.io.IOException

class ContactUsUseCase(private val userRepo: UserRepo) {

    suspend fun getContactUs(updateProfileBody: ContactUseBody): DataResource<Boolean> {
        if (updateProfileBody.name.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_name)))
        if (updateProfileBody.email.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_email)))
        if (updateProfileBody.mobile.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_mobile)))
        if (updateProfileBody.title.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_title)))
        if (updateProfileBody.message.isEmpty())
            return DataResource.Error(IOException(Injector.getApplicationContext().resources.getString(R.string.empty_message)))



        return userRepo.contactUs(updateProfileBody)
    }
}