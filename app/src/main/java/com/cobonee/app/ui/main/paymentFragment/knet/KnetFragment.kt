package com.cobonee.app.ui.main.paymentFragment.knet

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.cobonee.app.R
import kotlinx.android.synthetic.main.knet_fragment.*
import kotlinx.android.synthetic.main.visa_fragment.*
import kotlinx.android.synthetic.main.visa_fragment.spinner_month
import kotlinx.android.synthetic.main.visa_fragment.spinner_year

class KnetFragment : Fragment() {

    companion object {
        fun newInstance() = KnetFragment()
    }

    private lateinit var viewModel: KnetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.knet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(KnetViewModel::class.java)
        // TODO: Use the ViewModel
    }    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ArrayAdapter.createFromResource(requireContext(),R.array.month_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_month.adapter = adapter
            }
        ArrayAdapter.createFromResource(requireContext(),R.array.year_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_year.adapter = adapter
            }

        paymentKentConfirmMbtn.setOnClickListener {
            findNavController().navigate(R.id.operationCompletedFragment)
        }
    }

}
