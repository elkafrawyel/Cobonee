package com.cobonee.app.ui.main.paymentFragment.operationCompleted

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cobonee.app.R

class OperationCompletedFragment : Fragment() {

    companion object {
        fun newInstance() = OperationCompletedFragment()
    }

    private lateinit var viewModel: OperationCompletedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.operation_completed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OperationCompletedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
