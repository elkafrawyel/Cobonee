package com.cobonee.app.ui.main.aboutUsFragment.questionsFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController

import com.cobonee.app.R
import com.cobonee.app.ui.main.HomeActivity
import kotlinx.android.synthetic.main.about_us_fragment.*
import kotlinx.android.synthetic.main.questions_fragment.*

class QuestionsFragment : Fragment() {

    companion object {
        fun newInstance() = QuestionsFragment()
    }

    private lateinit var viewModel: QuestionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.questions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //test spinner array
        ArrayAdapter.createFromResource(requireContext(),R.array.test_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_reason.adapter = adapter
        }
    }

}
