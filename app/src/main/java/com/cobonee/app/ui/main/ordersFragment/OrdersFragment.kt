package com.cobonee.app.ui.main.ordersFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import kotlinx.android.synthetic.main.home_fragment.*
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
        // TODO: Use the ViewModel
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
