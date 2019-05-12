package com.cobonee.app.ui.main.favouritesFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import com.cobonee.app.entity.Offer
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.ui.main.homeFragment.AdapterOffers
import com.cobonee.app.ui.main.homeFragment.HomeFragmentDirections
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.favourites_fragment.*

class FavouritesFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {


    private lateinit var viewModel: FavouritesViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favouritesAdapter: AdapterOffers
    private var position: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourites_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onFavouritesResponse(it) })
        mainViewModel.removeAddOfferUiState.observe(this, Observer { onRemoveOfferResponse(it) })
        favouritesAdapter = AdapterOffers()
        favouritesAdapter.onItemChildClickListener = this
        favouritesRv.adapter = favouritesAdapter

        viewModel.getFavouriteList()
    }

    private fun onRemoveOfferResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                favouritesPb.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                favouritesPb.visibility = View.GONE
                activity?.snackBar(state.message, favouritesRootView)
            }
            MyUiStates.Success -> {
                if (this.position != -1) {
                    favouritesPb.visibility = View.GONE
                    viewModel.favouriteList.removeAt(position)
                    favouritesAdapter.notifyItemRemoved(position)
                }
            }
            MyUiStates.NoConnection -> {
                favouritesPb.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, favouritesRootView)
            }
            null -> {
            }
        }
    }


    private fun onFavouritesResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                favouritesPb.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                favouritesPb.visibility = View.GONE
                activity?.snackBar(state.message, favouritesRootView)
            }
            MyUiStates.Success -> {
                favouritesPb.visibility = View.GONE
                favouritesAdapter.replaceData(viewModel.favouriteList)
            }
            MyUiStates.NoConnection -> {
                favouritesPb.visibility = View.GONE
                activity?.snackBar(activity?.resources?.getString(R.string.no_connection_error)!!, favouritesRootView)
            }
            null -> {
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.offerCv -> {
                val action =
                    FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(adapter!!.data[position] as Offer)
                findNavController().navigate(action)
            }
            R.id.offerSaveImgv -> {
                this.position = position
                mainViewModel.removeOffer((adapter!!.data[position] as Offer).id!!)
            }
        }
    }
}
