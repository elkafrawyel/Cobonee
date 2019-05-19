package com.cobonee.app.ui.main.ordersFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersViewModel : CoboneeViewModel() {

    private var ordersJob: Job? = null

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
            withContext(dispatcherProvider.main) { showLoading() }
            val result = ordersUseCase.getOffers()
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        showSuccess(result.data)
                    }
                    is DataResource.Error -> showError(result.exception.message)
                }
            }
        }
    }

    private fun showLoading() {
        _ordersUiState.value = MyUiStates.Loading
    }

    private fun showSuccess(data: OffersResponse) {

        _ordersUiState.value = MyUiStates.Success
    }

    private fun showError(message: String?) {
        if (message != null)
            _ordersUiState.value = MyUiStates.Error(message)
        else
            _ordersUiState.value =
                MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_general))
    }
}
