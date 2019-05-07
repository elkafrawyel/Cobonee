package com.cobonee.app.ui.main.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.Department
import com.cobonee.app.entity.DepartmentResponse
import com.cobonee.app.entity.Offer
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
    private var lastPage: Int = 1
    var deptId: String = ""
    private var cityId: String = ""

    private var offersJob: Job? = null

    private val offersUseCase = Injector.getOffersUseCase()

    private val _offerUiState = MutableLiveData<OffersUiState>()
    val offersUiState: LiveData<OffersUiState>
        get() = _offerUiState

    var offersList: ArrayList<Offer> = arrayListOf()

    fun getOffers() {

        if (NetworkUtils.isWifiConnected()) {
            if (offersJob?.isActive == true) {
                return
            }
            page++
            if (page <= lastPage) {
                if (deptId != "" && cityId != "") {
                    offersJob = launchOffersJob(deptId, cityId)
                } else {
                    //department id ,city id not found
//                    showDepartmentsError(Injector.getApplicationContext().getString(R.string.error_city_or_dept))
                }


            } else {
                _offerUiState.value = OffersUiState.LastPage
            }
        } else {
            _offerUiState.value = OffersUiState.NoConnection
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
                showOffersLoading()
            }
            val result = offersUseCase.getOffers(deptId, cityId)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> showOffersSuccess(result.data)
                    is DataResource.Error -> showOffersError(result.exception.message)
                }
            }
        }
    }

    private fun showOffersLoading() {
        _offerUiState.value = OffersUiState.Loading
    }

    private fun showOffersSuccess(data: OffersResponse) {
        lastPage = data.meta.lastPage!!

        offersList.addAll(data.offers)
        _offerUiState.value = OffersUiState.Success
    }

    private fun showOffersError(message: String?) {
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


    //===============================================Departments======================================================

    private var departmentJob: Job? = null

    private val departmentsUseCase = Injector.getDepartmentsUseCase()

    private val _departmentsUiState = MutableLiveData<DepartmentsUiState>()
    val departmentsUiState: LiveData<DepartmentsUiState>
        get() = _departmentsUiState

    var departmentList: ArrayList<Department> = arrayListOf()

    fun getDepartments() {
        if (NetworkUtils.isWifiConnected()) {
            if (departmentJob?.isActive == true) {
                return
            }
            departmentJob = launchDepartmentsJob()
        } else {
            _departmentsUiState.value = DepartmentsUiState.NoConnection
        }
    }

    private fun launchDepartmentsJob(): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                showDepartmentsLoading()
            }
            val result = departmentsUseCase.getDepartments()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> showDepartmentsSuccess(result.data)
                    is DataResource.Error -> showDepartmentsError(result.exception.message)
                }
            }
        }
    }

    private fun showDepartmentsLoading() {
        _departmentsUiState.value = DepartmentsUiState.Loading
    }

    private fun showDepartmentsSuccess(data: DepartmentResponse) {
        departmentList.clear()
        departmentList.addAll(data.departments)
        _departmentsUiState.value = DepartmentsUiState.Success
    }

    private fun showDepartmentsError(message: String?) {
        if (message != null)
            _departmentsUiState.value = DepartmentsUiState.Error(message)
        else
            _departmentsUiState.value =
                DepartmentsUiState.Error(Injector.getApplicationContext().getString(R.string.error_offers))
    }

    sealed class DepartmentsUiState {
        object Loading : DepartmentsUiState()
        object Success : DepartmentsUiState()
        data class Error(val message: String) : DepartmentsUiState()
        object NoConnection : DepartmentsUiState()
    }

    //================================================================================================================

}
