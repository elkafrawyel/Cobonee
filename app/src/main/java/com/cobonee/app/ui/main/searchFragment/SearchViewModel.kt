package com.cobonee.app.ui.main.searchFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.entity.OffersResponse
import com.cobonee.app.ui.CoboneeViewModel
import com.cobonee.app.utily.DataResource
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel : CoboneeViewModel() {

    var page: Int = 0
    private var lastPage: Int = 1
    var query: String? = null
    private var searchJob: Job? = null

    private val searchUseCase = Injector.getSearchUseCase()

    private val _uiState = MutableLiveData<MyUiStates>()
    val uiState: LiveData<MyUiStates>
        get() = _uiState

    var offersList: ArrayList<Offer> = arrayListOf()

    private fun resetSearch() {
        page = 0
        lastPage = 1
        offersList.clear()
        query = null
    }

    fun newQuery(query: String) {
        resetSearch()
        this.query = query
        if (NetworkUtils.isWifiConnected()) {
            if (searchJob?.isActive == true) {
                searchJob!!.cancel()
            }
            page++
            searchJob = launchSearchJob(query,page )
        } else {
            _uiState.value = MyUiStates.NoConnection
        }
    }

    fun loadMore() {

        if (query != null) {
            if (NetworkUtils.isWifiConnected()) {
                if (searchJob?.isActive == true) {
                    searchJob!!.cancel()
                }
                page++
                if (page <= lastPage) {
                    searchJob = launchSearchJob(query!!, page)
                } else {
                    _uiState.value = MyUiStates.LastPage
                }
            } else {
                _uiState.value = MyUiStates.NoConnection
            }
        }
    }

    private fun launchSearchJob(query: String, page: Int): Job? {
        return scope.launch(dispatcherProvider.computation) {
            withContext(dispatcherProvider.main) {
                showOffersLoading()
            }
            val result = searchUseCase.search(query, page)
            withContext(dispatcherProvider.main) {
                when (result) {

                    is DataResource.Success -> {
                        lastPage = result.data.meta.lastPage!!
                        showOffersSuccess(result.data)
                    }
                    is DataResource.Error -> showOffersError(result.exception.message)
                }
            }
        }
    }

    private fun showOffersLoading() {
        _uiState.value = MyUiStates.Loading
    }

    private fun showOffersSuccess(data: OffersResponse) {
        offersList.addAll(data.offers)
        _uiState.value = MyUiStates.Success
    }

    private fun showOffersError(message: String?) {
        if (message != null) _uiState.value = MyUiStates.Error(message)
        else _uiState.value =
            MyUiStates.Error(Injector.getApplicationContext().getString(R.string.error_offers))
    }

}
