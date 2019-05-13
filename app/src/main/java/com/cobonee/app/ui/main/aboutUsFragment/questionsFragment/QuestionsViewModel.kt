package com.cobonee.app.ui.main.aboutUsFragment.questionsFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.ContactUseBody
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionsViewModel : CoboneeViewModel() {

    private var contactUsJob: Job? = null
    var contactUsResponse: String? = null



    private val contactUsUseCase = Injector.getContactUsUserUseCase()

    private val _contactUsUiState = MutableLiveData<MyUiStates>()
    val contactUsUiState: LiveData<MyUiStates>
        get() = _contactUsUiState

    fun sentMessage(updateProfileBody: ContactUseBody) {
        if (NetworkUtils.isWifiConnected()) {
            if (contactUsJob?.isActive == true) {
                return
            }
            contactUsJob = launchSettingsJob(updateProfileBody)
        } else {
            _contactUsUiState.value = MyUiStates.NoConnection
        }
    }


    private fun launchSettingsJob(updateProfileBody: ContactUseBody): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = contactUsUseCase.getContactUs(updateProfileBody)
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
        _contactUsUiState.value = MyUiStates.Loading
    }

    private fun showSuccess(data: String) {
        contactUsResponse = data
        _contactUsUiState.value = MyUiStates.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _contactUsUiState.value = MyUiStates.Error(message)
        else
            _contactUsUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }
}
