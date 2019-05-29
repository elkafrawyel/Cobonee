package com.cobonee.app.ui.main.cartFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.useCases.CreateOrderUseCase
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel : CoboneeViewModel() {
    private var job: Job? = null
    private fun getAllCartItems() = Injector.CartItemsUseCase()

    var totalPrice: Float? = 0F
    var cartItemsQuantityList: ArrayList<Int> = arrayListOf()

    private var _uiState = MutableLiveData<MyUiStates>()
    val uiState: LiveData<MyUiStates>
        get() = _uiState

    var cartList: ArrayList<Offer> = arrayListOf()

    fun getCartItems(offerIds: Array<Int>) {
        if (NetworkUtils.isWifiConnected()) {

            if (job?.isActive == true)
                return
            job = launchJob(offerIds)
        } else {
            _uiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchJob(offerIds: Array<Int>): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _uiState.value = MyUiStates.Loading }
            val result = getAllCartItems().getCartItems(offersId = offerIds)
            when (result) {
                is DataResource.Success -> {
//                    result.data.offers.forEachIndexed { index, offer ->
//                        offer.quantity = cartItemsQuantityList[index]
//                    }

                    withContext(dispatcherProvider.main) {
                        cartList.clear()
                        totalPrice = 0F

                        totalPrice = result.data.offers.sumByDouble { it.priceAfterDiscount!!.toDouble() }.toFloat()

                        cartList.addAll(result.data.offers)

                        _uiState.value = MyUiStates.Success

                    }
                }
                is DataResource.Error -> {
                    withContext(dispatcherProvider.main) {
                        _uiState.value = MyUiStates.Error(result.exception.message!!)
                    }
                }
            }
        }
    }

    //================================= createe Order =======================================================

    private var createOrderJob: Job? = null
    private fun CreateOrderUseCase() = Injector.CreateOrderUseCase()

    private var _ordersUiState = MutableLiveData<MyUiStates>()
    val ordersUiState: LiveData<MyUiStates>
        get() = _ordersUiState

    fun createOrder(ids: Array<Int>, quantities: Array<Int>) {
        if (NetworkUtils.isWifiConnected()) {

            if (createOrderJob?.isActive == true)
                return
            createOrderJob = launchCreateOrderJob(ids, quantities)
        } else {
            _ordersUiState.value = MyUiStates.NoConnection
        }
    }

    private fun launchCreateOrderJob(ids: Array<Int>, quantities: Array<Int>): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _ordersUiState.value = MyUiStates.Loading }
            val result = CreateOrderUseCase().createOrder(ids, quantities)
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> {
                        if (result.data.message=="success") {
                            _ordersUiState.value = MyUiStates.Success
                        }else{
                            _ordersUiState.value = MyUiStates.Error(Injector.getApplicationContext().getString(R.string.create_order_error))
                        }
                    }
                    is DataResource.Error -> {
                        _ordersUiState.value = MyUiStates.Error(result.exception.message!!)
                    }
                }
            }
        }
    }

}

