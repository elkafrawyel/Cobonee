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
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var offersAdapter: AdapterOffers

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


        mainViewModel.getSelectedCityLiveData().observe(this, Observer<City> { onCityChanged(it) })
        viewModel.offersUiState.observe(this, Observer { onOffersResponse(it) })
        viewModel.departmentsUiState.observe(this, Observer { onDepartmentResponse(it) })


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (!viewModel.firstOpen) {
                    viewModel.selectedTab = tab?.position!!
                } else {
                    viewModel.firstOpen = false
                    viewModel.selectedTab = 0
                }

                val deptId = viewModel.departmentList[viewModel.selectedTab].id

                viewModel.setDepartment(deptId = deptId.toString())

            }
        })
        viewModel.getDepartments()

        viewModel.getOffers()

    }

    private fun onCityChanged(city: City) {
        viewModel.setCity(cityId = city.id.toString())
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offersSwipe.setOnRefreshListener(this)

        offersSwipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        setUpAdapter()

    }

    private fun setUpAdapter() {
        offersAdapter = AdapterOffers().apply {
            setEnableLoadMore(true)
            openLoadAnimation(SLIDEIN_LEFT)
        }

        offersAdapter.onItemChildClickListener = this

        offersAdapter.setOnLoadMoreListener({ viewModel.getOffers() }, offersRv)

        offersAdapter.setEnableLoadMore(true)

        offersRv.adapter = offersAdapter
    }

    private fun onDepartmentResponse(state: HomeViewModel.DepartmentsUiState?) {
        when (state) {
            HomeViewModel.DepartmentsUiState.Loading -> {
                onDepartmentLoading()
            }
            HomeViewModel.DepartmentsUiState.Success -> {
                onDepartmentSuccess()
            }
            is HomeViewModel.DepartmentsUiState.Error -> {
                onDepartmentError(state.message)
            }
            HomeViewModel.DepartmentsUiState.NoConnection -> {
                onDepartmentNoConnection()
            }
            null -> {
            }
        }
    }

    private fun onDepartmentNoConnection() {
        offersSwipe.isRefreshing = false
        homePb.visibility = View.GONE
        activity?.snackBar(getString(R.string.no_connection_error), homeRootView)
    }

    private fun onDepartmentError(message: String) {
        homePb.visibility = View.GONE
        activity?.snackBar(message, homeRootView)
    }

    private fun onDepartmentSuccess() {
        homePb.visibility = View.GONE
        tabLayout.removeAllTabs()
        for (dept in viewModel.departmentList) {
            val tab = tabLayout.newTab()
            tab.text = dept.name
            tabLayout.addTab(tab)
        }
        tabLayout.selectTab(tabLayout.getTabAt(viewModel.selectedTab))
    }

    private fun onDepartmentLoading() {
        homePb.visibility = View.VISIBLE
        offersRv.visibility = View.GONE
        offersSwipe.isRefreshing = false
    }


    private fun onOffersResponse(state: HomeViewModel.OffersUiState) {
        when (state) {
            HomeViewModel.OffersUiState.Loading -> onOffersLoading()
            is HomeViewModel.OffersUiState.Success -> onOffersSuccess()
            is HomeViewModel.OffersUiState.Error -> onOffersError(state.message)
            HomeViewModel.OffersUiState.NoConnection -> onOffersNoConnection()
            HomeViewModel.OffersUiState.LastPage -> onOffersLastPage()
            is HomeViewModel.OffersUiState.NextPage -> onOffersNextPage()
        }
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

    private fun onOffersLastPage() {
        homePb.visibility = View.GONE
        offersAdapter.loadMoreEnd(true)
        offersSwipe.isRefreshing = false
    }

    private fun onOffersNextPage() {
        homePb.visibility = View.GONE
        offersRv.visibility = View.VISIBLE
        offersSwipe.isRefreshing = false
        offersAdapter.addData(viewModel.offersList)
        offersAdapter.loadMoreComplete()
    }

    private fun onOffersSuccess() {
        setUpAdapter()

        homePb.visibility = View.GONE
        offersRv.visibility = View.VISIBLE
        offersSwipe.isRefreshing = false
        offersAdapter.replaceData(viewModel.offersList)
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
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(adapter!!.data[position] as Offer)
                findNavController().navigate(action)
            }
            R.id.offerSaveImgv -> {
                requireContext().toast("Saved")
            }
        }
    }

    override fun onRefresh() {
        if (viewModel.departmentList.size == 0) {
            viewModel.getDepartments()
        }

        viewModel.newOffers()

        offersAdapter.setEnableLoadMore(true)
    }

}
