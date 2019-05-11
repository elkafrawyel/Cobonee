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
import com.cobonee.app.entity.Quetion
import com.cobonee.app.entity.Setting
import com.cobonee.app.entity.toQuetion
import com.cobonee.app.ui.main.aboutUsFragment.AboutUsViewModel
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.help_fragment.*

class HelpFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var viewModel: AboutUsViewModel
    private val questionsAdapter= AdapterQuestions()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.help_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        viewModel.settingsUiState.observe(this, Observer { onSettigsResponse(it) })
        viewModel.getSettings()
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        questionsAdapter.onItemChildClickListener = this

    }
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    private fun onSettigsResponse(state: AboutUsViewModel.LoginUiState?) {
        when (state) {
            AboutUsViewModel.LoginUiState.Loading -> {
                helpLoading.visibility = View.VISIBLE
                helpContentLayout.visibility = View.INVISIBLE
            }
            AboutUsViewModel.LoginUiState.Success -> {
                var settings: Setting?  = viewModel.settings
                if(settings!=null){
                    var quetionsList: ArrayList<Quetion> = arrayListOf()
                    quetionsList.add(settings.common_quetion_1.toQuetion(settings.common_quetion_1))
                    quetionsList.add(settings.common_quetion_2.toQuetion(settings.common_quetion_2))
                    quetionsList.add(settings.common_quetion_3.toQuetion(settings.common_quetion_3))
                    quetionsList.add(settings.common_quetion_4.toQuetion(settings.common_quetion_4))
                    questionsAdapter.replaceData(quetionsList)
                    questionsRv.adapter = questionsAdapter

                }
                helpLoading.visibility = View.GONE
                helpContentLayout.visibility = View.VISIBLE
            }
            is AboutUsViewModel.LoginUiState.Error -> {
                activity?.snackBar(state.message, rootView)
                helpLoading.visibility = View.GONE
            }
            AboutUsViewModel.LoginUiState.NoConnection -> {
                activity?.snackBar(resources.getString(R.string.no_connection_error), rootView)
                helpLoading.visibility = View.GONE
            }
            null -> {
            }
        }
    }
}
