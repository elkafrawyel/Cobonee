package com.cobonee.app.ui.main.paymentFragment.operationCompleted

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController

import com.cobonee.app.R
import com.cobonee.app.ui.main.HomeActivity

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
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            findNavController().popBackStack(R.id.homeFragment,false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack(R.id.homeFragment,false)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
