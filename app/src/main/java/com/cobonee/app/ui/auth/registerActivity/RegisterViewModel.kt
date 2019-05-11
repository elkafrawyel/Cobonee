package com.cobonee.app.ui.auth.registerActivity

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.User
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.ui.auth.loginActivity.LoginViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel : CoboneeViewModel() {

    private var registerJob: Job? = null
    private var user: User? = null

    private val registerUseCase = Injector.getRegisterUseCase()
    private val saveUserUseCase = Injector.getSaveUserUseCase()

    private val _registerUiState = MutableLiveData<RegisterUiState>()
    val registerUiState: LiveData<RegisterUiState>
        get() = _registerUiState

    private val _saveUserUI = MutableLiveData<LoginViewModel.SaveUserState>()
    val saveUserUI: LiveData<LoginViewModel.SaveUserState>
        get() = _saveUserUI

    fun register(name: String, username: String, password: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (registerJob?.isActive == true) {
                return
            }
            registerJob = launchRegisterJob(name, username, password)
        } else {
            _registerUiState.value = RegisterUiState.NoConnection
        }
    }

    private fun launchRegisterJob(
        name: String,
        username: String,
        password: String
    ): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = registerUseCase.register(name, username, password)
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
        _registerUiState.value = RegisterUiState.Loading
    }

    private fun showSuccess(data: LoginResponse) {
        user = User(
            data.data.id!!,
            data.token!!,
            data.data.name!!,
            data.data.email,
            data.data.city!!,
            data.data.mobile,
            data.data.gender
        )
        _registerUiState.value = RegisterUiState.Success
    }

    fun saveUser() {
        scope.launch(dispatcherProvider.computation) {
            val result = saveUserUseCase.save(user!!)
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> _saveUserUI.value = LoginViewModel.SaveUserState.Saved
                    is DataResource.Error -> _saveUserUI.value =
                        LoginViewModel.SaveUserState.Error(Injector.getApplicationContext().getString(R.string.error_general))
                }
            }
        }
    }

    private fun showError(message: String?) {
        if (message != null)
            _registerUiState.value = RegisterUiState.Error(message)
        else
            _registerUiState.value =
                RegisterUiState.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }

    sealed class RegisterUiState {
        object Loading : RegisterUiState()
        object Success : RegisterUiState()
        data class Error(val message: String) : RegisterUiState()
        object NoConnection : RegisterUiState()
    }

}
