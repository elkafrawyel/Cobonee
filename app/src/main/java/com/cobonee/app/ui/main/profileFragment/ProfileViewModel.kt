package com.cobonee.app.ui.main.profileFragment

import com.cobonee.app.entity.User
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job

class ProfileViewModel : CoboneeViewModel() {

    var canEditMain: Boolean = false
    var canEditExtra: Boolean = false


    private var updateProfileJob: Job? = null
    private var user: User? = null

    private val saveUserUseCase = Injector.getSaveUserUseCase()
    private val updateUserUseCase = Injector.getUpdateProfileUseCase()
    private val getUserUseCase = Injector.getUserUseCase()

    fun getUserData(): User {
        return getUserUseCase.get()
    }
}
