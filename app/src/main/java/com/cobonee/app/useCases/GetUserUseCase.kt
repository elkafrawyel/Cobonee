package com.cobonee.app.useCases

import com.cobonee.app.entity.User
import com.cobonee.app.repo.UserRepo
import com.cobonee.app.utily.DataResource

class GetUserUseCase (private val userRepo: UserRepo) {

    fun get(): User {

        return userRepo.getUser()
    }
}