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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter.*
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.observeEvent
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.home_fragment.*
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        if (!viewModel.opened) {
            viewModel.opened = true

            mainViewModel.getSelectedCityLiveData().observe(this, Observer<City> { onCityChanged(it) })
            mainViewModel.addOfferUiState.observeEvent(this) { myUiStates -> onAddOfferResponse(myUiStates) }
            mainViewModel.removeOfferUiState.observeEvent(this) { myUiStates -> onRemoveOfferResponse(myUiStates) }
            viewModel.offersUiState.observe(this, Observer { onOffersResponse(it) })
            viewModel.departmentsUiState.observe(this, Observer { onDepartmentResponse(it) })

            setUpAdapters()

        } else {

            setUpAdapters()

            onDepartmentSuccess()

            homeLoading.visibility = View.GONE
            emptyView.visibility = View.GONE
            offersRv.visibility = View.VISIBLE
            offersSwipe.isRefreshing = false
            offersAdapter.replaceData(viewModel.allOffersList)
            offersAdapter.loadMoreComplete()

        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            (activity as HomeActivity).homeBackClicked()
        }
    }

    private fun setUpAdapters() {
        departmentsAdapter = AdapterDepartment()

        departmentsAdapter.onItemChildClickListener = this
        departmentRv.adapter = departmentsAdapter

        offersAdapter = AdapterOffers().apply {
            setEnableLoadMore(true)
            setLoadMoreView(CustomLoadMoreView())
        }

        offersAdapter.onItemChildClickListener = this

        offersAdapter.setOnLoadMoreListener(
            { viewModel.getOffers(cityId = cityId, deptId = deptId, loadMore = true) },
            offersRv
        )

        offersAdapter.setEnableLoadMore(true)

        offersRv.adapter = offersAdapter

        offersSwipe.setOnRefreshListener(this)

        offersSwipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )

    }

    private fun onCityChanged(city: City) {
        offersAdapter.data.clear()
        offersAdapter.notifyDataSetChanged()
        cityId = city.id.toString()
        if (viewModel.departmentList.size == 0) {
            viewModel.getDepartments()
        } else {
            viewModel.getOffers(cityId, deptId)
        }
    }


    private fun onOfferSuccess() {
        homeLoading.visibility = View.GONE
        emptyView.visibility = View.GONE
        offersRv.visibility = View.VISIBLE
        offersSwipe.isRefreshing = false
        offersAdapter.addData(viewModel.offersList)
        offersAdapter.loadMoreComplete()
    }

    private fun onOffersResponse(state: MyUiStates) {
        when (state) {
            MyUiStates.Loading -> {
                homeLoading.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
                offersRv.visibility = View.VISIBLE
                offersSwipe.isRefreshing = false
            }
            is MyUiStates.Success -> {
                onOfferSuccess()
            }
            is MyUiStates.Error -> {
                offersAdapter.loadMoreFail()
                emptyView.visibility = View.GONE
                homeLoading.visibility = View.GONE
                offersSwipe.isRefreshing = false
                activity?.snackBar(state.message, homeRootView)
            }
            MyUiStates.NoConnection -> {
                offersSwipe.isRefreshing = false
                emptyView.visibility = View.GONE
                homeLoading.visibility = View.GONE
                activity?.snackBar(getString(R.string.no_connection_error), homeRootView)
            }
            MyUiStates.LastPage -> {
                homeLoading.visibility = View.GONE
                offersAdapter.loadMoreEnd()
                offersSwipe.isRefreshing = false
                emptyView.visibility = View.GONE

            }
            MyUiStates.Empty -> {
                emptyView.visibility = View.VISIBLE
                homeLoading.visibility = View.GONE
                offersSwipe.isRefreshing = false
            }
        }
    }

    private fun onRemoveOfferResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                homeLoading.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                homeLoading.visibility = View.GONE
                activity?.snackBar(state.message, homeRootView)
            }
            MyUiStates.Success -> {
                homeLoading.visibility = View.GONE
                viewModel.offersList[position].isFav = false
                offersAdapter.notifyItemChanged(position)
                activity?.snackBar(getString(R.string.remove_from_favourites), homeRootView)
            }
            MyUiStates.NoConnection -> {
                homeLoading.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, homeRootView)
            }
            null -> {
            }
        }
    }

    private fun onAddOfferResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                homeLoading.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                homeLoading.visibility = View.GONE
                activity?.snackBar(state.message, homeRootView)
            }
            MyUiStates.Success -> {
                homeLoading.visibility = View.GONE
                viewModel.offersList[position].isFav = true
                offersAdapter.notifyItemChanged(position)
                activity?.snackBar(getString(R.string.add_to_favourites), homeRootView)
            }
            MyUiStates.NoConnection -> {
                homeLoading.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, homeRootView)
            }
            null -> {
            }
        }
    }

    private fun onDepartmentResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                homeLoading.visibility = View.VISIBLE
                departmentRv.visibility = View.GONE
                offersSwipe.isRefreshing = false
            }
            MyUiStates.Success -> {

                onDepartmentSuccess()
                //click on first department
                clickOnRecyclerItem(0, departmentRv)
            }
            is MyUiStates.Error -> {
                offersSwipe.isRefreshing = false
                homeLoading.visibility = View.GONE
                activity?.snackBar(state.message, homeRootView)
            }
            MyUiStates.NoConnection -> {
                offersSwipe.isRefreshing = false
                homeLoading.visibility = View.GONE
                activity?.snackBar(getString(R.string.no_connection_error), homeRootView)
            }
            null -> {
            }
        }
    }

    private fun onDepartmentSuccess() {
        offersSwipe.isRefreshing = false
        homeLoading.visibility = View.GONE
        departmentRv.visibility = View.VISIBLE
        departmentsAdapter.addData(viewModel.departmentList)
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

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.offerCv, R.id.offerImgv -> {
                val bundle = Bundle()
                bundle.putParcelable("offer", viewModel.allOffersList[position])
                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
            }
            R.id.offerSaveImgv -> {
                val offer = viewModel.allOffersList[position]
                if (offer.isFav) {
                    mainViewModel.removeOffer(offer.id!!)
                } else {
                    mainViewModel.addOffer(offer.id!!)
                }
                this.position = position
            }

            R.id.departmentItem -> {
                val deptId = viewModel.departmentList[position].id

                for (i in 0 until viewModel.departmentList.size) {
                    departmentsAdapter.data[i].isSelected = i == position
                    viewModel.departmentList[i].isSelected = i == position
                }
                departmentsAdapter.notifyDataSetChanged()

                offersAdapter.data.clear()
                offersAdapter.notifyDataSetChanged()
                this@HomeFragment.deptId = deptId.toString()
                viewModel.getOffers(cityId = cityId, deptId = deptId.toString())
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
