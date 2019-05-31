package com.cobonee.app.ui.main.ordersFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.DataOrders
import com.cobonee.app.entity.Offer
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.entity.OrdersResponse
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersViewModel : CoboneeViewModel() {

    private var ordersJob: Job? = null
    var ordersList: ArrayList<DataOrders> = arrayListOf()
    private val ordersUseCase = Injector.getOrdersUseCase()

    private val _ordersUiState = MutableLiveData<MyUiStates>()
    val ordersUiState: LiveData<MyUiStates>
        get() = _ordersUiState

    fun getOrders() {
        if (NetworkUtils.isWifiConnected()) {
            if (ordersJob?.isActive == true) {
                return
            }
            ordersJob = launchOrdersJob()
        } else {
            _ordersUiState.value = MyUiStates.NoConnection
        }
    }


    private fun launchOrdersJob(): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                _ordersUiState.value = MyUiStates.Loading
            }
            val result = ordersUseCase.getOrders()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        if (result.data.data.isEmpty()) {
                            _ordersUiState.value = MyUiStates.Empty
                        } else {
                            ordersList.clear()
                            ordersList.addAll(result.data.data)
                            _ordersUiState.value = MyUiStates.Success
                        }
                    }
                    is DataResource.Error -> {
                        if (result.exception.message != null)
                            _ordersUiState.value = MyUiStates.Error(result.exception.message!!)
                        else
                            _ordersUiState.value =
                                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
                    }
                }
            }
        }
    }

    fun getMyOrdersSmallList(type: Int): ArrayList<DataOrders> {
        var smallList = ordersList
        if (type == 0) {
            smallList.removeAll { it.status != "new" }
        } else {
            smallList.removeAll { it.status == "new" }
        }
        return smallList
    }

}
