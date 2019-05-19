package com.cobonee.app.ui.main.ordersFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cobonee.app.R
import com.cobonee.app.utily.MyUiStates
import kotlinx.android.synthetic.main.orders_fragment.*

class OrdersFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = OrdersFragment()
    }

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
        viewModel.ordersUiState.observe(this, Observer { onOrdersResponseSuccess(it) })
        viewModel.getOrders()
        // TODO: Use the ViewModel
    }

    private fun onOrdersResponseSuccess(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {

            }
            MyUiStates.Success -> {

            }
            is MyUiStates.Error -> {

            }
            MyUiStates.NoConnection -> {

            }
            null -> {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ordersAdapter.addData("A")
        ordersAdapter.addData("B")
        ordersAdapter.addData("C")
        ordersAdapter.addData("A")
        ordersAdapter.addData("A")
        ordersAdapter.addData("A")

        ordersAdapter.onItemChildClickListener = this

        ordersRv.adapter = ordersAdapter


    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

}
