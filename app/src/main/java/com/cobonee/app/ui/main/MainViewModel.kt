package com.cobonee.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.City
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : CoboneeViewModel() {

    init {
        isLoggedIn()
    }

    fun logOut() {
        Injector.getPreferenceHelper().clear()
    }

    fun isLoggedIn(): Boolean {
        return Injector.getPreferenceHelper().isLoggedIn

    }

    //========================================== Cities ====================================
    private var citiesJob: Job? = null

    private val citiesUseCase = Injector.getCitiesUseCase()

    private val _citiesUiState = MutableLiveData<CitiesUiState>()
    val citiesUiState: LiveData<CitiesUiState>
        get() = _citiesUiState

    var citiesList: ArrayList<City> = arrayListOf()

    //to observe from fragment
    val selectedCity = MutableLiveData<City>()

    fun getSelectedCityLiveData(): LiveData<City> {
        return selectedCity
    }

    fun getCities() {
        if (NetworkUtils.isWifiConnected()) {
            if (citiesJob?.isActive == true) {
                return
            }
            citiesJob = launchCitiesJob()
        } else {
            _citiesUiState.value = CitiesUiState.NoConnection
        }
    }

    private fun launchCitiesJob(): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) { showLoading() }
            val result = citiesUseCase.getCities()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> showSuccess(result.data)
                    is DataResource.Error -> showError(result.exception.message)
                }
            }
        }
    }

    private fun showLoading() {
        _citiesUiState.value = CitiesUiState.Loading
    }

    private fun showSuccess(data: CityResponse) {
        citiesList.clear()
        citiesList.addAll(data.cities)
        _citiesUiState.value = CitiesUiState.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _citiesUiState.value = CitiesUiState.Error(message)
        else
            _citiesUiState.value =
                CitiesUiState.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }

    sealed class CitiesUiState {
        object Loading : CitiesUiState()
        object Success : CitiesUiState()
        data class Error(val message: String) : CitiesUiState()
        object NoConnection : CitiesUiState()
    }

    //======================================================================================


}