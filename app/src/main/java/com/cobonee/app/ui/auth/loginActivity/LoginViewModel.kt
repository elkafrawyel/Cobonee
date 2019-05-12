package com.cobonee.app.ui.auth.loginActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.entity.User
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : CoboneeViewModel() {

    private var loginJob: Job? = null
    private var user: User? = null

    private val loginUseCase = Injector.getLoginUseCase()
    private val saveUserUseCase = Injector.getSaveUserUseCase()

    private val _loginUiState = MutableLiveData<MyUiStates>()
    val loginUiState: LiveData<MyUiStates>
        get() = _loginUiState


    private val _saveUserUI = MutableLiveData<MyUiStates>()
    val saveUserUI: LiveData<MyUiStates>
        get() = _saveUserUI

    fun login(username: String, password: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (loginJob?.isActive == true) {
                return
            }
            loginJob = launchLoginJob(username, password)
        } else {
            _loginUiState.value = MyUiStates.NoConnection
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
        _loginUiState.value = MyUiStates.Loading
    }

    private fun showSuccess(data: LoginResponse) {

        user = User(
            data.data.id!!,
            data.token!!,
            data.data.name!!,
            data.data.email!!,
            data.data.city ?: City(),
            data.data.mobile ?: "",
            data.data.gender ?: ""
        )

        _loginUiState.value = MyUiStates.Success
    }

    fun saveUser() {
        scope.launch(dispatcherProvider.computation) {
            val result = saveUserUseCase.save(user!!)
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> _saveUserUI.value = MyUiStates.Success
                    is DataResource.Error -> _saveUserUI.value =
                        MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                }
            }
        }
    }

    private fun showError(message: String?) {
        if (message != null)
            _loginUiState.value = MyUiStates.Error(message)
        else
            _loginUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }
}
