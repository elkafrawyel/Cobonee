package com.cobonee.app.ui.main.cartFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import com.cobonee.app.entity.CartItem
import com.cobonee.app.entity.Coubone
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.observeEvent
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.cart_fragment.*

class CartFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var viewModel: CartViewModel
    private lateinit var mainViewModel: MainViewModel
    private val cartAdapter = AdapterCartItems()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

        mainViewModel.getCartItemsLiveData().observe(this, Observer { onCartItems(it) })
        mainViewModel.removeCartItemsUiState.observeEvent(this) { onRemoveCartItems(it) }

        viewModel.ordersUiState.observe(this, Observer { onCreateOrderResponse(it) })
        viewModel.uiState.observe(this, Observer { onCartItemsResponse(it) })
    }

    private fun onCreateOrderResponse(states: MyUiStates?) {
        when(states){
            MyUiStates.Loading -> {
                cartLoading.visibility = View.VISIBLE
            }

            MyUiStates.Success -> {
                cartLoading.visibility = View.GONE
                activity?.snackBar(resources.getString(R.string.cart_order_created), cartRootView)
            }

            is MyUiStates.Error -> {
                cartLoading.visibility = View.GONE
                activity?.snackBar(states.message, cartRootView)
            }

            MyUiStates.NoConnection -> {
                cartLoading.visibility = View.GONE
                activity?.snackBar(resources.getString(R.string.no_connection_error), cartRootView)
            }

            null -> {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter.onItemChildClickListener = this

        cartRv.adapter = cartAdapter

        next.setOnClickListener {
            //            findNavController().navigate(R.id.paymentFragment)
            val ids = viewModel.cartList.map { it.id }.toTypedArray()
            val quantities = viewModel.cartList.map { 1 }.toTypedArray()

            viewModel.createOrder(ids.filterNotNull().toTypedArray(), quantities.filterNotNull().toTypedArray())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            findNavController().navigateUp()

        }
    }

    private fun onRemoveCartItems(states: MyUiStates) {
        when (states) {
            MyUiStates.Loading -> {
                cartLoading.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                cartLoading.visibility = View.GONE
                cartAdapter.data.clear()
                activity?.snackBar(resources.getString(R.string.cart_item_removed), cartRootView)
            }
            MyUiStates.LastPage -> {

            }
            is MyUiStates.Error -> {
                cartLoading.visibility = View.GONE
                activity?.snackBar(states.message, cartRootView)
            }
            MyUiStates.NoConnection -> {
                cartLoading.visibility = View.GONE
                activity?.snackBar(resources.getString(R.string.no_connection_error), cartRootView)
            }
            MyUiStates.Empty -> {

            }
        }
    }

    private fun onCartItemsResponse(states: MyUiStates?) {
        when (states) {
            MyUiStates.Loading -> {
                cartLoading.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                cartLoading.visibility = View.GONE
                cartAdapter.addData(viewModel.cartList)
                listCount.text = viewModel.cartList.size.toString()
                total_cart_content.text = viewModel.totalPrice?.toString()
                val charges = 30
                chargesTv.text = charges.toString()
                total.text = (viewModel.totalPrice?.plus(charges)).toString()
            }
            MyUiStates.LastPage -> {

            }
            is MyUiStates.Error -> {

            }
            MyUiStates.NoConnection -> {
                cartLoading.visibility = View.GONE
                activity?.snackBar(resources.getString(R.string.no_connection_error), cartRootView)
            }
            MyUiStates.Empty -> {

            }
            null -> {

            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.icon_delete -> {
                mainViewModel.removeCartItems((adapter?.data!![position] as Coubone).id!!)
            }

//            R.id.increaseCoboneeQuantity -> {
//                viewModel.cartList[position].quantity = viewModel.cartList[position].quantity.plus(1)
//                cartAdapter.notifyDataSetChanged()
//            }
//            R.id.decreaseCoboneeQuantity -> {
//                if (viewModel.cartList[position].quantity > 0) {
//                    viewModel.cartList[position].quantity = viewModel.cartList[position].quantity.minus(1)
//                    cartAdapter.notifyDataSetChanged()
//                }
//            }
        }
    }

    private fun onCartItems(list: List<CartItem>?) {
        val coubonesIdList = list?.map { it.itemId }?.toTypedArray()
        viewModel.cartItemsQuantityList.clear()
        list?.map { it.itemQuentity }?.toCollection(viewModel.cartItemsQuantityList)
        viewModel.getCartItems(coubonesIdList!!)
    }


}
