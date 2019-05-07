package com.cobonee.app.ui.main.homeFragment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.*
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.*

class HomeFragment : Fragment(), OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener,
    RequestLoadMoreListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private val offersAdapter = AdapterOffers()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        //activity lifecycle owner not fragment
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        viewModel.offersUiState.observe(this, Observer { onOffersResponse(it) })

        mainViewModel.getSelectedCityLiveData().observe(this, Observer<City> { it ->
            viewModel.setParameters(cityId = it.id.toString())
            resetOffers()
            viewModel.getOffers()
        })


        viewModel.setParameters(deptId = "1")
        resetOffers()
        viewModel.getOffers()
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        for (i in 1..8) {
            var tab = tabLayout.newTab()
            tab.text = "Tab $i"

            tabLayout.addTab(tab)
        }

        offersSwipe.setOnRefreshListener(this)

        offersSwipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        offersAdapter.onItemChildClickListener = this

        offersAdapter.setOnLoadMoreListener(this, offersRv)

        offersAdapter.openLoadAnimation(SLIDEIN_LEFT)

        offersRv.adapter = offersAdapter

    }

    fun resetOffers(){
        viewModel.offersList.clear()
        offersRv.recycledViewPool.clear()
        offersAdapter.data.clear()
        offersAdapter.notifyDataSetChanged()
    }

    private fun onOffersResponse(state: HomeViewModel.OffersUiState) {
        when (state) {
            HomeViewModel.OffersUiState.Loading -> onOffersLoading()
            is HomeViewModel.OffersUiState.Success -> onOffersSuccess()
            is HomeViewModel.OffersUiState.Error -> onOffersError(state.message)
            HomeViewModel.OffersUiState.NoConnection -> onOffersNoConnection()
            HomeViewModel.OffersUiState.LastPage -> onOffersEnded()
        }
    }

    private fun onOffersEnded() {
        homePb.visibility = View.GONE
        offersAdapter.loadMoreEnd()
        offersSwipe.isRefreshing = false
    }

    private fun onOffersNoConnection() {
        offersSwipe.isRefreshing = false
        homePb.visibility = View.GONE
        activity?.snackBar(getString(R.string.no_connection_error), homeRootView)
    }

    private fun onOffersError(message: String) {
        offersAdapter.loadMoreFail()
        homePb.visibility = View.GONE
        offersSwipe.isRefreshing = false
        activity?.snackBar(message, homeRootView)
    }

    private fun onOffersSuccess() {

        homePb.visibility = View.GONE
        offersRv.visibility = View.VISIBLE
        offersSwipe.isRefreshing = false

        if (viewModel.offersList.isEmpty()) {
            offersAdapter.emptyView = layoutInflater.inflate(R.layout.empty_view, null, false)
        } else {
            offersAdapter.replaceData(viewModel.offersList)
        }

        offersAdapter.loadMoreComplete()
    }

    private fun onOffersLoading() {
        homePb.visibility = View.VISIBLE
        offersRv.visibility = View.VISIBLE
        offersSwipe.isRefreshing = false

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.offerCv -> {
                findNavController().navigate(R.id.detailsFragment)
            }
            R.id.offerSaveImgv -> {
                requireContext().toast("Saved")
            }
        }
    }

    override fun onLoadMoreRequested() {
        viewModel.getOffers()
    }

    override fun onRefresh() {

        offersRv.recycledViewPool.clear()

        viewModel.offersList.clear()

        offersAdapter.notifyDataSetChanged()

        viewModel.refreshOffers()

        //when refresh enable reload pages
        offersAdapter.setEnableLoadMore(true)
    }
}
