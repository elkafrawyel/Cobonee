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
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var offersAdapter: AdapterOffers
    private var position: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    private fun onCityChanged(city: City) {
        viewModel.setCity(cityId = city.id.toString())
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        //activity lifecycle owner not fragment
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)


        mainViewModel.getSelectedCityLiveData().observe(this, Observer<City> { onCityChanged(it) })
        viewModel.offersUiState.observe(this, Observer { onOffersResponse(it) })
        viewModel.departmentsUiState.observe(this, Observer { onDepartmentResponse(it) })
        mainViewModel.addAddOfferUiState.observe(this, Observer { onAddOfferResponse(it) })
        mainViewModel.removeAddOfferUiState.observe(this, Observer { onRemoveOfferResponse(it) })

        offersSwipe.setOnRefreshListener(this)

        offersSwipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        setUpAdapter()



        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (viewModel.lastSelectedTab != null) {
                    if (tab?.position == viewModel.lastSelectedTab) {
                        val deptId = viewModel.departmentList[tab?.position!!].id
                        viewModel.setDepartment(deptId = deptId.toString())
                    }
                } else {
                    val deptId = viewModel.departmentList[tab?.position!!].id
                    viewModel.setDepartment(deptId = deptId.toString())
                }
            }
        })

        if (!viewModel.started) {
            viewModel.started = true

            viewModel.getDepartments()

            viewModel.getOffers()
        } else {
            //opened Before
            onOffersSuccess()
        }
    }

    private fun onRemoveOfferResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                homePb.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                homePb.visibility = View.GONE
                activity?.snackBar(state.message, homeRootView)
            }
            MyUiStates.Success -> {
                homePb.visibility = View.GONE
                viewModel.offersList[position].isSaved = false
                offersAdapter.notifyItemChanged(position)
                activity?.snackBar(getString(R.string.remove_from_favourites), homeRootView)
            }
            MyUiStates.NoConnection -> {
                homePb.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, homeRootView)
            }
            null -> { }
        }
    }

    private fun onAddOfferResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                homePb.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                homePb.visibility = View.GONE
                activity?.snackBar(state.message, homeRootView)
            }
            MyUiStates.Success -> {
                homePb.visibility = View.GONE
                viewModel.offersList[position].isSaved = true
                offersAdapter.notifyItemChanged(position)
                activity?.snackBar(getString(R.string.add_to_favourites), homeRootView)
            }
            MyUiStates.NoConnection -> {
                homePb.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, homeRootView)
            }
            null -> { }
        }
    }

    private fun setUpAdapter() {
        offersAdapter = AdapterOffers().apply {
            setEnableLoadMore(true)
        }

        offersAdapter.onItemChildClickListener = this

        offersAdapter.setOnLoadMoreListener({ viewModel.getOffers() }, offersRv)

        offersAdapter.setEnableLoadMore(true)

        offersRv.adapter = offersAdapter
    }

    private fun onDepartmentResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                onDepartmentLoading()
            }
            MyUiStates.Success -> {
                onDepartmentSuccess()
            }
            is MyUiStates.Error -> {
                onDepartmentError(state.message)
            }
            MyUiStates.NoConnection -> {
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
        var lastTab: TabLayout.Tab? = null
        viewModel.departmentList.forEachIndexed { index, dept ->
            val tab = tabLayout.newTab()
            tab.text = dept.name
            tabLayout.addTab(tab)

            if (viewModel.lastSelectedTab != null && index == viewModel.lastSelectedTab) {
                lastTab = tab
            }
        }

        if (lastTab != null) {
            lastTab?.select()
        }

    }

    private fun onDepartmentLoading() {
        homePb.visibility = View.VISIBLE
        offersRv.visibility = View.GONE
        offersSwipe.isRefreshing = false
    }

    private fun onOffersResponse(state: MyUiStates) {
        when (state) {
            MyUiStates.Loading -> onOffersLoading()
            is MyUiStates.Success -> onOffersSuccess()
            is MyUiStates.Error -> onOffersError(state.message)
            MyUiStates.NoConnection -> onOffersNoConnection()
            MyUiStates.LastPage -> onOffersLastPage()
//            is HomeViewModel.OffersUiState.NextPage -> onOffersNextPage()
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

    override fun onDestroyView() {
        super.onDestroyView()
        saveCurrentTab()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.offerCv -> {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(adapter!!.data[position] as Offer)
                findNavController().navigate(action)
                saveCurrentTab()
            }
            R.id.offerSaveImgv -> {
                val offer =(adapter!!.data[position] as Offer)
                if(offer.isSaved){
                    mainViewModel.removeOffer(offer.id!!)

                }else{
                    mainViewModel.addOffer(offer.id!!)
                }
                this.position = position
            }
        }
    }

    private fun saveCurrentTab() {
        viewModel.lastSelectedTab = tabLayout.selectedTabPosition
    }

    override fun onRefresh() {
        if (viewModel.departmentList.size == 0) {
            viewModel.getDepartments()
        }

        if (mainViewModel.citiesList.size == 0) {
            mainViewModel.getCities()
        }

        viewModel.newOffers()

        offersAdapter.setEnableLoadMore(true)
    }

}
