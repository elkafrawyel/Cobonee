package com.cobonee.app.ui.auth.loginActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : CoboneeViewModel() {
    // TODO: Implement the ViewModel

    private var loginJob: Job? = null

    private val loginUseCase = Injector.getLoginUseCase()

    private val _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState: LiveData<LoginUiState>
        get() = _loginUiState


    var loginResponse: LoginResponse? = null;

    fun getLogin(username: String, password: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (loginJob?.isActive == true) {
                return
            }
            loginJob = launchLoginJob(username, password)
        } else {
            _loginUiState.value = LoginUiState.NoConnection
        }
    }

    private fun launchLoginJob(username: String, password: String): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = loginUseCase.getLogin(username, password)
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
        _loginUiState.value = LoginUiState.Loading
    }

    private fun showSuccess(data: LoginResponse) {
        loginResponse = data
        _loginUiState.value = LoginUiState.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _loginUiState.value = LoginUiState.Error(message)
        else
            _loginUiState.value =
                LoginUiState.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }

    sealed class LoginUiState {
        object Loading : LoginUiState()
        object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        object NoConnection : LoginUiState()
    }
}
