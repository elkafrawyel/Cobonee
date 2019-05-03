package com.cobonee.app.ui.main.paymentFragment.visa

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController

import com.cobonee.app.R
import kotlinx.android.synthetic.main.visa_fragment.*
import java.util.*

class VisaFragment : Fragment() {

    companion object {
        fun newInstance() = VisaFragment()
    }

    private lateinit var viewModel: VisaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.visa_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        changeLanguage(requireContext(),"en")
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VisaViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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


        paymentVisaConfirmMbtn.setOnClickListener {
            findNavController().navigate(R.id.operationCompletedFragment)
        }

    }

}
