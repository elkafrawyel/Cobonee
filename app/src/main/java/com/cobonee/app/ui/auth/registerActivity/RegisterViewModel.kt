package com.cobonee.app.ui.auth.registerActivity

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

class RegisterViewModel : CoboneeViewModel() {

    private var registerJob: Job? = null
    private var citiesJob: Job? = null

    private var user: User? = null
    var citiesList: ArrayList<City> = arrayListOf()


    private val registerUseCase = Injector.getRegisterUseCase()
    private val saveUserUseCase = Injector.getSaveUserUseCase()
    private val citiesUseCase = Injector.getCitiesUseCase()

    private val _registerUiState = MutableLiveData<MyUiStates>()
    val registerUiState: LiveData<MyUiStates>
        get() = _registerUiState

    private val _saveUserUI = MutableLiveData<MyUiStates>()
    val saveUserUI: LiveData<MyUiStates>
        get() = _saveUserUI

    private val _citiesUiState = MutableLiveData<MyUiStates>()
    val citiesUiState: LiveData<MyUiStates>
        get() = _citiesUiState

    fun register(name: String, username: String, password: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (registerJob?.isActive == true) {
                return
            }
            registerJob = launchRegisterJob(name, username, password)
        } else {
            _registerUiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchRegisterJob(
        name: String,
        username: String,
        password: String
    ): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { _registerUiState.value = MyUiStates.Loading }
            val result = registerUseCase.register(name, username, password)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        showSuccess(result.data)
                    }
                    is DataResource.Error -> {
                        if (result.exception.message != null)
                            _registerUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _registerUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                    }
                }
            }
        }
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
        _registerUiState.value = MyUiStates.Success
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

    fun getCities() {
        if (NetworkUtils.isWifiConnected()) {
            if (citiesJob?.isActive == true) {
                return
            }
            citiesJob = launchCitiesJob()
        } else {
            _citiesUiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchCitiesJob(): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { _citiesUiState.value = MyUiStates.Loading }
            val result = citiesUseCase.getCities()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        citiesList.clear()
                        citiesList.addAll(result.data.cities)
                        _citiesUiState.value = MyUiStates.Success
                    }
                    is DataResource.Error -> {
                        if (result.exception.message != null)
                            _citiesUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _citiesUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                    }
                }
            }
        }
    }

}
