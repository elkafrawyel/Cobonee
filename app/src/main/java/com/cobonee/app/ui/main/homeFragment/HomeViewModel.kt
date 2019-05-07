package com.cobonee.app.ui.main.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : CoboneeViewModel() {

    //============================================== Offers ==========================================================

    var page: Int = 0
    var lastPage: Int = 1
    var deptId: String = ""
    var cityId: String = ""

    private var offersJob: Job? = null

    private val offersUseCase = Injector.getOffersUseCase()

    private val _offerUiState = MutableLiveData<OffersUiState>()
    val offersUiState: LiveData<OffersUiState>
        get() = _offerUiState

    var offersList: ArrayList<String> = arrayListOf()

    fun getOffers() {
        if (page <= lastPage) {
            if (NetworkUtils.isWifiConnected()) {
                if (offersJob?.isActive == true) {
                    return
                }
                if (deptId != "" && cityId != "") {
                    page++
                    offersJob = launchOffersJob(deptId, cityId)
                } else {
                    //department id ,city id not found
//                    showError(Injector.getApplicationContext().getString(R.string.error_city_or_dept))
                }
            } else {
                _offerUiState.value = OffersUiState.NoConnection
            }
        } else {
            _offerUiState.value = OffersUiState.LastPage
        }
    }

    fun setParameters(deptId: String = this.deptId, cityId: String = this.cityId) {
        if (deptId != "") {
            this.deptId = deptId
        }

        if (cityId != "") {
            this.cityId = cityId

        }

        if (deptId != "" && cityId != "") {
            refreshOffers()
        }
    }

    fun refreshOffers() {
        page = 0
        lastPage = 1
        getOffers()
    }

    private fun launchOffersJob(deptId: String, cityId: String): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                showLoading()
            }
            val result = offersUseCase.getOffers(deptId, cityId)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> showSuccess(result.data)
                    is DataResource.Error -> showError(result.exception.message)
                }
            }
        }
    }

    private fun showLoading() {
        _offerUiState.value = OffersUiState.Loading
    }

    private fun showSuccess(data: OffersResponse) {
//        lastPage = data.meta.lastPage!!
        lastPage = 5
//        page = data.meta.currentPage!!

        offersList.add("A")
        offersList.add("A")
        offersList.add("A")
        offersList.add("A")
        offersList.add("A")


//        citiesList = data.offers
        _offerUiState.value = OffersUiState.Success
    }

    private fun showError(message: String?) {
        if (message != null) _offerUiState.value = OffersUiState.Error(message)
        else _offerUiState.value =
            OffersUiState.Error(Injector.getApplicationContext().getString(R.string.error_offers))
    }

    sealed class OffersUiState {
        object Loading : OffersUiState()
        object Success : OffersUiState()
        data class Error(val message: String) : OffersUiState()
        object NoConnection : OffersUiState()
        object LastPage : OffersUiState()
    }


    //================================================================================================================
}
