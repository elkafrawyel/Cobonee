package com.cobonee.app.ui.auth.forgetPassword

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

class ForgetViewModel : CoboneeViewModel() {

    private var forgetJob: Job? = null
    private var user: User? = null
    public var newToken: String? = null

    private val forgetUseCase = Injector.getForgetUseCase()
    private val saveUserUseCase = Injector.getSaveUserUseCase()

    private val _forgetUiState = MutableLiveData<MyUiStates>()
    val forgetUiState: LiveData<MyUiStates>
        get() = _forgetUiState


    private val _saveUserUI = MutableLiveData<MyUiStates>()
    val saveUserUI: LiveData<MyUiStates>
        get() = _saveUserUI

    fun forget(username: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (forgetJob?.isActive == true) {
                return
            }
            forgetJob = launchForgetJob(username)
        } else {
            _forgetUiState.value = MyUiStates.NoConnection
        }
    }


    private fun launchForgetJob(username: String): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {  _forgetUiState.value = MyUiStates.Loading }
            val result = forgetUseCase.getForget(username)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        _forgetUiState.value = MyUiStates.Success
                    }
                    is DataResource.Error -> {
                        if (result.exception.message != null)
                            _forgetUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _forgetUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                    }
                }
            }
        }
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


    //    ===================================================== check - code ===================
    private var checkJob: Job? = null

    private val checkUseCase = Injector.getForgetUseCase()

    private val _checkUiState = MutableLiveData<MyUiStates>()
    val checkUiState: LiveData<MyUiStates>
        get() = _checkUiState


    fun check(username: String, code :String) {
        if (NetworkUtils.isWifiConnected()) {
            if (checkJob?.isActive == true) {
                return
            }
            checkJob = launchCheckJob(username,code)
        } else {
            _checkUiState.value = MyUiStates.NoConnection
        }
    }


    private fun launchCheckJob(username: String, code :String): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {  _checkUiState.value = MyUiStates.Loading }
            val result = checkUseCase.getCheck(username,code)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        newToken = result.data.token
                        _checkUiState.value = MyUiStates.Success
                    }
                    is DataResource.Error ->{

                        if (result.exception.message != null)
                            _checkUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _checkUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                    }
                }
            }
        }
    }
    //    ===================================================== reset ===================
    private var resetJob: Job? = null

    private val resetUseCase = Injector.getForgetUseCase()

    private val _resetUiState = MutableLiveData<MyUiStates>()
    val resetUiState: LiveData<MyUiStates>
        get() = _resetUiState


    fun reset(token: String, password: String, password_confirmation: String) {
        if (NetworkUtils.isWifiConnected()) {
            if (resetJob?.isActive == true) {
                return
            }
            resetJob = launRsetJob(token,password,password_confirmation)
        } else {
            _resetUiState.value = MyUiStates.NoConnection
        }
    }


    private fun launRsetJob(token: String, password: String, password_confirmation: String): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {  _resetUiState.value = MyUiStates.Loading }
            val result = resetUseCase.getReset(token,password,password_confirmation)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        var data: LoginResponse = result.data
                        user = User(
                            data.data.id!!,
                            data.token!!,
                            data.data.name!!,
                            data.data.email!!,
                            data.data.city ?: City(),
                            data.data.mobile ?: "",
                            data.data.gender ?: ""
                        )
                        _resetUiState.value = MyUiStates.Success
                    }
                    is DataResource.Error ->{

                        if (result.exception.message != null)
                            _resetUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _resetUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                    }
                }
            }
        }
    }
}
