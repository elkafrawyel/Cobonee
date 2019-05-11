package com.cobonee.app.ui.main.aboutUsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.*
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AboutUsViewModel : CoboneeViewModel() {

    private var settingsJob: Job? = null
    var settings: Setting? = null
    var questions: ArrayList<Quetion> = arrayListOf()

    private val settingsUseCase = Injector.getSettingsUseCase()

    private val _settingsUiState = MutableLiveData<LoginUiState>()
    val settingsUiState: LiveData<LoginUiState>
        get() = _settingsUiState

    fun getSettings() {
        if (NetworkUtils.isWifiConnected()) {
            if (settingsJob?.isActive == true) {
                return
            }
            settingsJob = launchSettingsJob()
        } else {
            _settingsUiState.value = LoginUiState.NoConnection
        }
    }


    private fun launchSettingsJob(): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = settingsUseCase.getSettings()
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
        _settingsUiState.value = LoginUiState.Loading
    }

    private fun showSuccess(data: Setting) {
        settings = data
        questions.add(data.common_quetion_1.toQuetion(data.common_quetion_1))
        questions.add(data.common_quetion_2.toQuetion(data.common_quetion_2))
        questions.add(data.common_quetion_3.toQuetion(data.common_quetion_3))
        questions.add(data.common_quetion_4.toQuetion(data.common_quetion_4))
        _settingsUiState.value = LoginUiState.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _settingsUiState.value = LoginUiState.Error(message)
        else
            _settingsUiState.value =
                LoginUiState.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }

    sealed class LoginUiState {
        object Loading : LoginUiState()
        object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
        object NoConnection : LoginUiState()
    }

}
