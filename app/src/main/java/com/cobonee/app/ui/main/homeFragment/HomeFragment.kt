package com.cobonee.app.ui.main.homeFragment

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.*
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.entity.Department
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.observeEvent
import com.cobonee.app.utily.snackBar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.util.*

class HomeFragment : Fragment(), OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var offersAdapter: AdapterOffers
    private lateinit var departmentsAdapter: AdapterDepartment
    private var position: Int = -1
    private var cityId: String? = null
    private var deptId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)


        return view
    }

    private fun onCityChanged(city: City) {
        offersAdapter.data.clear()
        offersAdapter.notifyDataSetChanged()
        cityId = city.id.toString()
        if (deptId != null)
            viewModel.getOffers(cityId = cityId, deptId = deptId)
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        mainViewModel.getSelectedCityLiveData().observe(this, Observer<City> { onCityChanged(it) })
        viewModel.offersUiState.observe(this, Observer { onOffersResponse(it) })
        viewModel.departmentsUiState.observe(this, Observer { onDepartmentResponse(it) })
        mainViewModel.addOfferUiState.observeEvent(this) { myUiStates -> onAddOfferResponse(myUiStates) }
        mainViewModel.removeOfferUiState.observeEvent(this) { myUiStates -> onRemoveOfferResponse(myUiStates) }

        if (viewModel.departmentList.size == 0)
            viewModel.getDepartments()

        offersSwipe.setOnRefreshListener(this)

        offersSwipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

        setUpAdapter()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            (activity as HomeActivity).homeBackClicked()
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
                viewModel.offersList[position].isFav = false
                offersAdapter.notifyItemChanged(position)
                activity?.snackBar(getString(R.string.remove_from_favourites), homeRootView)
            }
            MyUiStates.NoConnection -> {
                homePb.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, homeRootView)
            }
            null -> {
            }
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
                viewModel.offersList[position].isFav = true
                offersAdapter.notifyItemChanged(position)
                activity?.snackBar(getString(R.string.add_to_favourites), homeRootView)
            }
            MyUiStates.NoConnection -> {
                homePb.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, homeRootView)
            }
            null -> {
            }
        }
    }

    private fun setUpAdapter() {

        departmentsAdapter = AdapterDepartment()

        departmentsAdapter.onItemChildClickListener = OnItemChildClickListener { adapter, view, position ->
            val deptId = viewModel.departmentList[position].id

            for (i in 0 until viewModel.departmentList.size) {
                departmentsAdapter.data[i].isSelected = i == position
                viewModel.departmentList[i].isSelected = i == position
            }
            departmentsAdapter.notifyDataSetChanged()

            offersAdapter.data.clear()
            offersAdapter.notifyDataSetChanged()
            this@HomeFragment.deptId = deptId.toString()
            if (cityId != null)
                viewModel.getOffers(cityId = cityId, deptId = deptId.toString())
        }

        departmentRv.adapter = departmentsAdapter

        offersAdapter = AdapterOffers().apply {
            setEnableLoadMore(true)
        }

        offersAdapter.onItemChildClickListener = this

        offersAdapter.setOnLoadMoreListener({
            viewModel.getOffers(cityId = cityId, deptId = deptId, loadMore = true)
        }, offersRv)

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
        offersSwipe.isRefreshing = false
        homePb.visibility = View.GONE
        activity?.snackBar(message, homeRootView)
    }

    private fun onDepartmentSuccess() {
        offersSwipe.isRefreshing = false
        homePb.visibility = View.GONE
        departmentRv.visibility = View.VISIBLE
        departmentsAdapter.addData(viewModel.departmentList)

        //click on first department
        clickOnRecyclerItem(0, departmentRv)
    }

    private fun clickOnRecyclerItem(position: Int, recyclerView: RecyclerView) {
        Objects.requireNonNull<RecyclerView.LayoutManager>(recyclerView.layoutManager).scrollToPosition(position)
        Handler().postDelayed({
            activity?.runOnUiThread {
                val holder = recyclerView.findViewHolderForAdapterPosition(position)
                holder?.itemView?.performClick()
            }
        }, 200)
    }

    private fun onDepartmentLoading() {
        homePb.visibility = View.VISIBLE
        departmentRv.visibility = View.GONE
        offersSwipe.isRefreshing = false
    }

    private fun onOffersResponse(state: MyUiStates) {
        when (state) {
            MyUiStates.Loading -> onOffersLoading()
            is MyUiStates.Success -> onOffersSuccess()
            is MyUiStates.Error -> onOffersError(state.message)
            MyUiStates.NoConnection -> onOffersNoConnection()
            MyUiStates.LastPage -> onOffersLastPage()
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
        homePb.visibility = View.GONE
        offersRv.visibility = View.VISIBLE
        offersSwipe.isRefreshing = false
        offersAdapter.addData(viewModel.offersList)
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
                val bundle = Bundle()
                bundle.putParcelable("offer", adapter!!.data[position] as Offer)
                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
            }
            R.id.offerSaveImgv -> {
                val offer = (adapter!!.data[position] as Offer)
                if (offer.isFav) {
                    mainViewModel.removeOffer(offer.id!!)

                } else {
                    mainViewModel.addOffer(offer.id!!)
                }
                this.position = position
            }
        }
    }

    override fun onRefresh() {
        offersAdapter.data.clear()
        if (viewModel.departmentList.size == 0) {
            viewModel.getDepartments()
        }
        if (mainViewModel.citiesList.size == 0) {
            mainViewModel.getCities()
        }
        offersAdapter.data.clear()
        viewModel.getOffers(cityId = cityId, deptId = deptId)
        offersAdapter.setEnableLoadMore(true)
    }

}
