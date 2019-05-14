package com.cobonee.app.ui.main.aboutUsFragment.questionsFragment

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

class QuestionsViewModel : CoboneeViewModel() {

    private var contactUsJob: Job? = null

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
                        showSuccess()
                    }
                    is DataResource.Error -> showError(result.exception.message)
                }
            }
        }
    }

    private fun showLoading() {
        _contactUsUiState.value = MyUiStates.Loading
    }

    private fun showSuccess() {
        _contactUsUiState.value = MyUiStates.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _contactUsUiState.value = MyUiStates.Error(message)
        else
            _contactUsUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }
    //========================================== Cities ====================================
    private var reasonsJob: Job? = null

    private val reasonsUseCase = Injector.getReasonsUseCase()

    private val _reasonsUiState = MutableLiveData<MyUiStates>()
    val reasonsUiState: LiveData<MyUiStates>
        get() = _reasonsUiState

    var reasonsList: ArrayList<Reason> = arrayListOf()

    //to observe from fragment

    fun getReasons() {
        if (NetworkUtils.isWifiConnected()) {
            if (reasonsJob?.isActive == true) {
                return
            }
            reasonsJob = launchCitiesJob()
        } else {
            _reasonsUiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchCitiesJob(): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showReasonsLoading() }
            val result = reasonsUseCase.getReasons()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> showReasonsSuccess(result.data)
                    is DataResource.Error -> showReasonsError(result.exception.message)
                }
            }
        }
    }

    private fun showReasonsLoading() {
        _reasonsUiState.value = MyUiStates.Loading
    }

    private fun showReasonsSuccess(data: ReasonsResponse) {
        reasonsList.clear()
        reasonsList.addAll(data.reasons)
        _reasonsUiState.value = MyUiStates.Success
    }

    private fun showReasonsError(message: String?) {
        if (message != null)
            _reasonsUiState.value = MyUiStates.Error(message)
        else
            _reasonsUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }

    //======================================================================================
}
