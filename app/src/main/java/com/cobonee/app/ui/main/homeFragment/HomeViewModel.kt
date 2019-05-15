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
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : CoboneeViewModel() {

    //===========================================Load Data Configurations ===============================================

    var started: Boolean = false
    var lastSelectedTab: Int? = null

    //=================================================================================================================

    //============================================== Offers ==========================================================
    var page: Int = 0
    private var lastPage: Int = 1
    var deptId: String = ""
    private var cityId: String = ""

    private var offersJob: Job? = null

    private val offersUseCase = Injector.getOffersUseCase()

    private val _offerUiState = MutableLiveData<MyUiStates>()
    val offersUiState: LiveData<MyUiStates>
        get() = _offerUiState

    var offersList: ArrayList<Offer> = arrayListOf()

    fun setDepartment(deptId: String = this.deptId) {
        if (lastSelectedTab != null) {
            lastSelectedTab = null
            return
        }


        if (deptId != "" && this.deptId != deptId) {
            this.deptId = deptId
            if (this.cityId != "") {
                newOffers()
            }
        }
    }

    fun setCity(cityId: String = this.cityId) {
        if (cityId != "" && this.cityId != cityId) {
            this.cityId = cityId
            if (this.deptId != "") {
                newOffers()
            }
        }
    }

    fun newOffers() {
        page = 0
        lastPage = 1
        offersList.clear()
        getOffers()
    }

    fun getOffers() {

        if (NetworkUtils.isWifiConnected()) {
            if (offersJob?.isActive == true) {
                offersJob!!.cancel()
            }
            page++
            if (page <= lastPage) {
                if (deptId != "" && cityId != "") {
                    offersJob = launchOffersJob(deptId, cityId, page)
                } else {
                    //department id ,cityName id not found
//                    showDepartmentsError(Injector.getApplicationContext().getString(R.string.error_city_or_dept))
                }
            } else {
                _offerUiState.value = MyUiStates.LastPage
            }
        } else {
            _offerUiState.value = MyUiStates.NoConnection
        }

    }

    private fun launchOffersJob(deptId: String, cityId: String, page: Int): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                showOffersLoading()
            }
            val result = offersUseCase.getOffers(deptId, cityId, page)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        lastPage = result.data.meta.lastPage!!
//                        lastPage = 3
//                        if (this@HomeViewModel.page == 1) {
                        showOffersSuccess(result.data)
//                        } else {
//                            showOffersNextPage(result.data)
//                        }
                    }
                    is DataResource.Error -> showOffersError(result.exception.message)
                }
            }
        }
    }

    private fun showOffersLoading() {
        _offerUiState.value = MyUiStates.Loading
    }

    private fun showOffersSuccess(data: OffersResponse) {
        offersList.clear()
        offersList.addAll(data.offers)
        _offerUiState.value = MyUiStates.Success
    }

//    private fun showOffersNextPage(data: OffersResponse) {
//        offersList.addAll(data.offers)
//        _offerUiState.value = OffersUiState.NextPage(offersList)
//    }

    private fun showOffersError(message: String?) {
        if (message != null) _offerUiState.value = MyUiStates.Error(message)
        else _offerUiState.value =
            MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_offers))
    }

    //================================================================================================================


    //===============================================Departments======================================================

    private var departmentJob: Job? = null

    private val departmentsUseCase = Injector.getDepartmentsUseCase()

    private val _departmentsUiState = MutableLiveData<MyUiStates>()
    val departmentsUiState: LiveData<MyUiStates>
        get() = _departmentsUiState

    var departmentList: ArrayList<Department> = arrayListOf()

    fun getDepartments() {
        if (NetworkUtils.isWifiConnected()) {
            if (departmentJob?.isActive == true) {
                return
            }
            departmentJob = launchDepartmentsJob()
        } else {
            _departmentsUiState.value = MyUiStates.NoConnection
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
        _departmentsUiState.value = MyUiStates.Loading
    }

    private fun showDepartmentsSuccess(data: DepartmentResponse) {
        departmentList.clear()
        departmentList.addAll(data.departments)

        _departmentsUiState.value = MyUiStates.Success
    }

    private fun showDepartmentsError(message: String?) {
        if (message != null)
            _departmentsUiState.value = MyUiStates.Error(message)
        else
            _departmentsUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_offers))
    }

    //================================================================================================================

}
