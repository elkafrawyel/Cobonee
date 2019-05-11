package com.cobonee.app.ui.main.aboutUsFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.entity.Quetion
import com.cobonee.app.entity.Setting
import com.cobonee.app.entity.toQuetion
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.about_us_fragment.*
import kotlinx.android.synthetic.main.activity_home.*

class AboutUsFragment : Fragment() {

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    private lateinit var viewModel: AboutUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        viewModel.settingsUiState.observe(this, Observer { onSettigsResponse(it) })
        viewModel.getSettings()
        // TODO: Use the ViewModel
    }

    private fun onSettigsResponse(state: AboutUsViewModel.LoginUiState?) {
        when (state) {
            AboutUsViewModel.LoginUiState.Loading -> {
                aboutLoading.visibility = View.VISIBLE
                aboutContentLayout.visibility = View.INVISIBLE
            }
            AboutUsViewModel.LoginUiState.Success -> {
                var settings: Setting?  = viewModel.settings
                if(settings?.about_us!=null){
                        text_about_cobonee.text= settings.about_us.content
                    cobonee_phone.text = settings.mobile
                    cobonee_email.text = settings.email

                    var quetionsList: ArrayList<Quetion> = arrayListOf()
                    quetionsList.add(settings.common_quetion_1.toQuetion(settings.common_quetion_1))
                    quetionsList.add(settings.common_quetion_2.toQuetion(settings.common_quetion_2))
                    quetionsList.add(settings.common_quetion_3.toQuetion(settings.common_quetion_3))
                    quetionsList.add(settings.common_quetion_4.toQuetion(settings.common_quetion_4))
                }
                aboutLoading.visibility = View.GONE
                aboutContentLayout.visibility = View.VISIBLE
            }
            is AboutUsViewModel.LoginUiState.Error -> {
                activity?.snackBar(state.message, rootView)
                aboutLoading.visibility = View.GONE
            }
            AboutUsViewModel.LoginUiState.NoConnection -> {
                activity?.snackBar(resources.getString(R.string.no_connection_error), rootView)
                aboutLoading.visibility = View.GONE
            }
            null -> {
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHereTv.setOnClickListener {
            (requireActivity() as HomeActivity).setHomeTitle(resources.getString(R.string.label_questions))
            findNavController().navigate(R.id.questionsFragment)
        }
    }
}
