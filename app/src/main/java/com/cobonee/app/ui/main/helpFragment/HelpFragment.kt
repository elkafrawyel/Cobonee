package com.cobonee.app.ui.main.helpFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter

import com.cobonee.app.R
import com.cobonee.app.ui.main.aboutUsFragment.SettingsViewModel
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.help_fragment.*

class HelpFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private val questionsAdapter = AdapterQuestions()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.help_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        viewModel.settingsUiState.observe(this, Observer { onSettingsResponse(it) })
        viewModel.getSettings()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionsAdapter.onItemChildClickListener = this
        questionsRv.adapter = questionsAdapter

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }

    private fun onSettingsResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                helpLoading.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                questionsAdapter.replaceData(viewModel.questions)
                helpLoading.visibility = View.GONE
            }
            is MyUiStates.Error -> {
                activity?.snackBar(state.message, rootView)
                helpLoading.visibility = View.GONE
            }
            MyUiStates.NoConnection -> {
                activity?.snackBar(resources.getString(R.string.no_connection_error), rootView)
                helpLoading.visibility = View.GONE
            }
            null -> {
            }
        }
    }
}
