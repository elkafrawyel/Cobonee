package com.cobonee.app.ui.main.aboutUsFragment

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

class SettingsViewModel : CoboneeViewModel() {

    private var settingsJob: Job? = null
    var settings: Setting? = null
        var questions: ArrayList<Quetion> = arrayListOf()

    private val settingsUseCase = Injector.getSettingsUseCase()

    private val _settingsUiState = MutableLiveData<MyUiStates>()
    val settingsUiState: LiveData<MyUiStates>
        get() = _settingsUiState

    fun getSettings() {
        if (NetworkUtils.isWifiConnected()) {
            if (settingsJob?.isActive == true) {
                return
            }
            settingsJob = launchSettingsJob()
        } else {
            _settingsUiState.value = MyUiStates.NoConnection
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
        _settingsUiState.value = MyUiStates.Loading
    }

    private fun showSuccess(data: Setting) {
        settings = data
        questions.addAll(data.quetions)
        _settingsUiState.value = MyUiStates.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _settingsUiState.value = MyUiStates.Error(message)
        else
            _settingsUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }
}
