package com.cobonee.app.ui.main.favouritesFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel : CoboneeViewModel() {
    private var job: Job? = null
    private fun getFavouritesUseCase() = Injector.getFavouritesUseCase()


    private var _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState>
        get() = _uiState

    var favouriteList: ArrayList<Offer> = arrayListOf()

    fun getFavouriteList() {
        if (NetworkUtils.isWifiConnected()) {

            if (job?.isActive == true)
                return
            job = launchJob()
        } else {
            _uiState.value = UiState.NoConnection
        }
    }


    private fun launchJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _uiState.value = UiState.Loading }
            val result = getFavouritesUseCase().getFavourites()
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> {
                        favouriteList.clear()
                        favouriteList.addAll(result.data)
                        _uiState.value = UiState.Success
                    }
                    is DataResource.Error -> {
                        _uiState.value = UiState.Error(result.exception.message!!)
                    }
                }
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Error(val message: String) : UiState()
        object Success : UiState()
        object NoConnection : UiState()
    }
}
