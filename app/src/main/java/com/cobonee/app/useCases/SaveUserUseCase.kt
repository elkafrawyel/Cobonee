package com.cobonee.app.useCases

import com.cobonee.app.entity.User
import com.cobonee.app.repo.UserRepo
import com.cobonee.app.utily.DataResource

class SaveUserUseCase(private val userRepo: UserRepo) {

    fun save(user:User): DataResource<Boolean> {

        return userRepo.saveUser(user)
    }
}