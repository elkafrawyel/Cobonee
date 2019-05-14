package com.cobonee.app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.CityResponse
import com.cobonee.app.entity.City
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Event
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
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

    private val _citiesUiState = MutableLiveData<MyUiStates>()
    val citiesUiState: LiveData<MyUiStates>
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
            _citiesUiState.value = MyUiStates.NoConnection
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
        _citiesUiState.value = MyUiStates.Loading
    }

    private fun showSuccess(data: CityResponse) {
        citiesList.clear()
        citiesList.addAll(data.cities)
        _citiesUiState.value = MyUiStates.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _citiesUiState.value = MyUiStates.Error(message)
        else
            _citiesUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }

    //======================================================================================


    //====================================== Add to Favourites =============================
    private var addJob: Job? = null

    private fun getAddOfferToFavouritesUseCase() = Injector.getAddOfferToFavouritesUseCase()

    private var _addUiState = MutableLiveData<Event<MyUiStates>>()
    val addAddOfferUiState: LiveData<Event<MyUiStates>>
        get() = _addUiState

    fun addOffer(offerId: Int) {
        if (NetworkUtils.isWifiConnected()) {
            if (addJob?.isActive == true)
                return
            addJob = launchAddJob(offerId)
        } else {
            _addUiState.value = Event(MyUiStates.NoConnection)
        }
    }


    private fun launchAddJob(offerId: Int): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _addUiState.value = Event(MyUiStates.Loading) }
            val result = getAddOfferToFavouritesUseCase().addOffer(offerId = offerId)
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> {
                        _addUiState.value = Event(MyUiStates.Success)
                    }
                    is DataResource.Error -> {
                        _addUiState.value = Event(MyUiStates.Error(result.exception.message!!))
                    }
                }
            }
        }
    }

    //======================================================================================

    //====================================== Remove to Favourites =============================
    private var removeJob: Job? = null

    private fun getRemoveOfferToFavouritesUseCase() = Injector.getRemoveOfferToFavouritesUseCase()

    private var _removeUiState = MutableLiveData<Event<MyUiStates>>()
    val removeAddOfferUiState: LiveData<Event<MyUiStates>>
        get() = _removeUiState

    fun removeOffer(offerId: Int) {
        if (NetworkUtils.isWifiConnected()) {
            if (removeJob?.isActive == true)
                return
            removeJob = launchRemoveJob(offerId)
        } else {
            _removeUiState.value = Event(MyUiStates.NoConnection)
        }
    }


    private fun launchRemoveJob(offerId: Int): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _removeUiState.value = Event(MyUiStates .Loading) }
            val result = getRemoveOfferToFavouritesUseCase().removeOffer(offerId = offerId)
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> {
                        _removeUiState.value = Event(MyUiStates.Success)
                    }
                    is DataResource.Error -> {
                        _removeUiState.value = Event(MyUiStates .Error(result.exception.message!!))
                    }
                }
            }
        }
    }

    //======================================================================================

}