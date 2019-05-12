package com.cobonee.app.ui.main.favouritesFragment

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

class FavouritesViewModel : CoboneeViewModel() {
    private var job: Job? = null
    private fun getFavouritesUseCase() = Injector.getFavouritesUseCase()


    private var _uiState = MutableLiveData<MyUiStates>()
    val uiState: LiveData<MyUiStates>
        get() = _uiState

    var favouriteList: ArrayList<Offer> = arrayListOf()

    fun getFavouriteList() {
        if (NetworkUtils.isWifiConnected()) {

            if (job?.isActive == true)
                return
            job = launchJob()
        } else {
            _uiState.value = MyUiStates.NoConnection
        }
    }


    private fun launchJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            withContext(dispatcherProvider.main) { _uiState.value = MyUiStates.Loading }
            val result = getFavouritesUseCase().getFavourites()
            withContext(dispatcherProvider.main) {
                when (result) {
                    is DataResource.Success -> {
                        favouriteList.clear()
                        favouriteList.addAll(result.data)
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
