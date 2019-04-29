package com.cobonee.app.ui.main.homeFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import com.cobonee.app.utily.toast
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
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
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        offersAdapter.addData("A")
        offersAdapter.addData("B")
        offersAdapter.addData("C")
        offersAdapter.addData("A")
        offersAdapter.addData("A")
        offersAdapter.addData("A")

        offersAdapter.onItemChildClickListener = this

        offersRv.adapter = offersAdapter

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when(view?.id){
            R.id.offerCv -> { findNavController().navigate(R.id.detailsFragment) }
            R.id.offerSaveImgv -> { requireContext().toast("Saved")}
        }
    }

}
