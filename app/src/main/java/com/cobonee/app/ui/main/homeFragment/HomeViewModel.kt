package com.cobonee.app.ui.main.homeFragment

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.Department
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : CoboneeViewModel() {

    var opened: Boolean = false
    var layoutManagerStateOffers: Parcelable? = null
    var layoutManagerStateDepartment: Parcelable? = null
    //============================================== Offers ==========================================================
    var page: Int = 0
    private var lastPage: Int = 1

    private var offersJob: Job? = null

    private val offersUseCase = Injector.getOffersUseCase()

    private val _offerUiState = MutableLiveData<MyUiStates>()
    val offersUiState: LiveData<MyUiStates>
        get() = _offerUiState

    var offersList: ArrayList<Offer> = arrayListOf()
    var allOffersList: ArrayList<Offer> = arrayListOf()

    private fun refresh() {
        page = 0
        lastPage = 1
        offersList.clear()
        allOffersList.clear()
    }

    fun getOffers(cityId: String?, deptId: String?, loadMore: Boolean = false) {

        if (!loadMore) {
            refresh()
        }

        if (deptId == null || cityId == null) {
            return
        }

        if (NetworkUtils.isWifiConnected()) {
            if (offersJob?.isActive == true) {
                offersJob!!.cancel()
            }
            page++
            if (page <= lastPage) {
                offersJob = launchOffersJob(deptId, cityId, page, loadMore)
            } else {
                _offerUiState.value = MyUiStates.LastPage
            }
        } else {
            _offerUiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchOffersJob(deptId: String, cityId: String, page: Int, loadMore: Boolean): Job? {
        return scope.launch(dispatcherProvider.computation) {
            if (!loadMore) {
                withContext(dispatcherProvider.main) {
                    _offerUiState.value = MyUiStates.Loading
                }
            }
            val result = offersUseCase.getOffers(deptId, cityId, page)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        lastPage = result.data.meta.lastPage!!
                        if (result.data.offers.isEmpty()) {
                            _offerUiState.value = MyUiStates.Empty
                        } else {
                            offersList.clear()
                            offersList.addAll(result.data.offers)
                            allOffersList.addAll(result.data.offers)
                            _offerUiState.value = MyUiStates.Success
                        }
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
//                        departmentList.add(Department(1, "One", arrayListOf(), false))
//                        departmentList.add(Department(1, "Two", arrayListOf(), false))
//                        departmentList.add(Department(1, "Three", arrayListOf(), false))
//                        departmentList.add(Department(1, "Four", arrayListOf(), false))
//                        departmentList.add(Department(1, "Five", arrayListOf(), false))
//                        departmentList.add(Department(1, "Six", arrayListOf(), false))
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
