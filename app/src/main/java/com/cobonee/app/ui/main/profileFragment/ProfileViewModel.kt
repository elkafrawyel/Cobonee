package com.cobonee.app.ui.main.profileFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.*
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : CoboneeViewModel() {


    init {
        getUserData()
    }
    var canEditMain: Boolean = false
    var canEditExtra: Boolean = false
    var user: User? = null

    private var updateProfileJob: Job? = null


    private val saveUserUseCase = Injector.getSaveUserUseCase()
    private val updateUserUseCase = Injector.getUpdateProfileUseCase()

    private fun getUserData() {
        user = Injector.getUserUseCase().get()
    }

    private val _updateProfileUiState = MutableLiveData<MyUiStates>()
    val updateProfileUiState: LiveData<MyUiStates>
        get() = _updateProfileUiState

    fun updateProfile(updateProfileBody: UpdateProfileBody) {
        if (NetworkUtils.isWifiConnected()) {
            if (updateProfileJob?.isActive == true) {
                return
            }
            updateProfileJob = launchUpdateProfileJob(updateProfileBody)
        } else {
            _updateProfileUiState.value = MyUiStates.NoConnection
        }
    }


    private fun launchUpdateProfileJob(updateProfileBody: UpdateProfileBody): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = updateUserUseCase.update(updateProfileBody)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        showSuccess(result.data)
                    }
                    is DataResource.Error -> showError(result.exception.message)
                }
            }
        }
    }

    private fun showLoading() {
        _updateProfileUiState.value = MyUiStates.Loading
    }

    private fun showSuccess(data: UpdateProfileResponse) {

        user = User(
            data.data.id!!,
            Injector.getUserUseCase().get().token,
            data.data.name!!,
            data.data.email!!,
            data.data.city ?: City(),
            data.data.mobile ?: "",
            data.data.gender ?: ""
        )
        saveUser()
        _updateProfileUiState.value = MyUiStates.Success
    }

    private fun saveUser() {
        scope.launch(dispatcherProvider.computation) {
            saveUserUseCase.save(user!!)
        }
    }

    private fun showError(message: String?) {
        if (message != null)
            _updateProfileUiState.value = MyUiStates.Error(message)
        else
            _updateProfileUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }
}
