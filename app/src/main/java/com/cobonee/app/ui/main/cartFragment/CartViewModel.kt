package com.cobonee.app.ui.main.cartFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel : CoboneeViewModel() {
    private var job: Job? = null
    private fun getAllCartItems() = Injector.CartItemsUseCase()


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
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> {
                        cartList.clear()
                        cartList.addAll(result.data.offers)
                        _uiState.value = MyUiStates.Success
                    }
                    is DataResource.Error -> {
                        _uiState.value = MyUiStates.Error(result.exception.message!!)
                    }
                }
            }
        }
    }
}
