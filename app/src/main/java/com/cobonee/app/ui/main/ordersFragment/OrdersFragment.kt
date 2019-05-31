package com.cobonee.app.ui.main.ordersFragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cobonee.app.R
import com.cobonee.app.entity.DataOrders
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.orders_fragment.*
import kotlinx.android.synthetic.main.orders_fragment.emptyView

class OrdersFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private var type: Int = 0
    private lateinit var viewModel: OrdersViewModel
    private val ordersAdapter = AdapterOrders()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.orders_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
        viewModel.ordersUiState.observe(this, Observer { onOrdersResponse(it) })
        viewModel.getOrders()
        // TODO: Use the ViewModel
    }


    private fun onOrdersResponse(state: MyUiStates) {
        when (state) {
            MyUiStates.Loading -> {
                ordersLoading.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
            }
            is MyUiStates.Success -> {
                ordersLoading.visibility = View.GONE
                emptyView.visibility = View.GONE
                addDataToAdapter()
            }
            is MyUiStates.Error -> {
                emptyView.visibility = View.GONE
                ordersLoading.visibility = View.GONE
                activity?.snackBar(state.message, ordersRootView)
            }
            MyUiStates.NoConnection -> {
                emptyView.visibility = View.GONE
                ordersLoading.visibility = View.GONE
                activity?.snackBar(getString(R.string.no_connection_error), ordersRootView)
            }
            MyUiStates.Empty -> {
                emptyView.visibility = View.VISIBLE
                ordersLoading.visibility = View.GONE
            }
        }
    }

    private fun addDataToAdapter() {
        if (viewModel.ordersList.size > 0) {
            ordersAdapter.data.clear()
            ordersAdapter.addData(viewModel.getMyOrdersSmallList(type))
        } else {
            viewModel.getOrders()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersAdapter.onItemChildClickListener = this

        ordersRv.adapter = ordersAdapter

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                type = tab?.position!!
                addDataToAdapter()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

}
