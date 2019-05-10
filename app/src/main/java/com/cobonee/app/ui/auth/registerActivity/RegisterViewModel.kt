package com.cobonee.app.ui.auth.registerActivity

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

class RegisterViewModel : CoboneeViewModel() {
    // TODO: Implement the ViewModel

    private var registerJob: Job? = null

    private val registerUseCase = Injector.getRegisterUseCase()

    private val _registerUiState = MutableLiveData<RegisterUiState>()
    val registerUiState: LiveData<RegisterUiState>
        get() = _registerUiState


    var registerResponse: LoginResponse? = null;

    fun getRegister(name: String, username: String, password: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (registerJob?.isActive == true) {
                return
            }
            registerJob = launchRegisterJob(name,username, password)
        } else {
            _registerUiState.value = RegisterUiState.NoConnection
        }
    }

    private fun launchRegisterJob(name: String, username: String, password: String): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = registerUseCase.getRegister(name,username, password)
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
        registerResponse = data
        _registerUiState.value = RegisterUiState.Success
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
