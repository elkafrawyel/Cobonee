package com.cobonee.app.ui.main.homeFragment

import androidx.fragment.app.Fragment
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

    val fragmentSavedStates = mutableMapOf<String, Fragment.SavedState?>()


    //============================================== Offers ==========================================================
    var page: Int = 0
    private var lastPage: Int = 1
    var deptId: String? = null
    var deptIndex: Int? = null
    var cityId: String? = null

    private var offersJob: Job? = null

    private val offersUseCase = Injector.getOffersUseCase()

    private val _offerUiState = MutableLiveData<MyUiStates>()
    val offersUiState: LiveData<MyUiStates>
        get() = _offerUiState

    var offersList: ArrayList<Offer> = arrayListOf()

    fun refresh() {
        page = 0
        lastPage = 1
        offersList.clear()
    }

    fun getOffers() {

        if (deptId == null || cityId == null) {
            return
        }

        if (NetworkUtils.isWifiConnected()) {
            if (offersJob?.isActive == true) {
                offersJob!!.cancel()
            }
            page++
            if (page <= lastPage) {
                offersJob = launchOffersJob(deptId!!, cityId!!, page)
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
                        showOffersSuccess(result.data)
                    }
                    is DataResource.Error -> {
                        if (result.exception.message != null) {
                            _offerUiState.value = MyUiStates.Error(result.exception.message!!)
                        } else {
                            _offerUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_offers))
                        }
                    }
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

    private fun showOffersError(message: String?) {

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
                _departmentsUiState.value = MyUiStates.Loading
            }
            val result = departmentsUseCase.getDepartments()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        departmentList.clear()
                        departmentList.addAll(result.data.departments)

                        _departmentsUiState.value = MyUiStates.Success
                    }
                    is DataResource.Error -> {
                        if (result.exception.message != null)
                            _departmentsUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _departmentsUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_offers))
                    }
                }
            }
        }
    }

    //================================================================================================================

}
