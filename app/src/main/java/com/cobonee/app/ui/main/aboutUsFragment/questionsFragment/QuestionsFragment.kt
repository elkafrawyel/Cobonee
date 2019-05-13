package com.cobonee.app.ui.main.aboutUsFragment.questionsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.cobonee.app.R
import com.cobonee.app.entity.ContactUseBody
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
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
        viewModel.contactUsUiState.observe(this, Observer { onContactUsResponse(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //test spinner array
        ArrayAdapter.createFromResource(requireContext(), R.array.test_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner_reason.adapter = adapter
            }

        sendMessage.setOnClickListener {
            viewModel.sentMessage(
                ContactUseBody(
                    edit_name.text.toString(),
                    edit_email.text.toString(),
                    spinner_reason.selectedItem.toString(),
                    edit_subject.text.toString(),
                    edit_message.text.toString(),
                    edit_phone.text.toString()
                )
            )
        }
    }

    private fun onContactUsResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                contactLoading.visibility = View.VISIBLE
                sendMessage.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {
                findNavController().popBackStack()
                findNavController().navigate(R.id.homeFragment)
                activity?.snackBar(resources.getString(R.string.message_send), questionsRootView)
                contactLoading.visibility = View.GONE
                sendMessage.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                activity?.snackBar(state.message, questionsRootView)
                contactLoading.visibility = View.GONE
                sendMessage.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                activity?.snackBar(resources.getString(R.string.no_connection_error), questionsRootView)
                contactLoading.visibility = View.GONE
                sendMessage.visibility = View.VISIBLE
            }
            null -> {
            }
        }

    }

}
