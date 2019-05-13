package com.cobonee.app.ui.main.aboutUsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.cobonee.app.R
import com.cobonee.app.entity.Setting
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.about_us_fragment.*

class AboutUsFragment : Fragment() {

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.about_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        viewModel.settingsUiState.observe(this, Observer { onSettigsResponse(it) })
        viewModel.getSettings()
    }

    private fun onSettigsResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                aboutLoading.visibility = View.VISIBLE
                aboutContentLayout.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {
                val settings: Setting?  = viewModel.settings
                if(settings?.about_us!=null){
                        text_about_cobonee.text= settings.about_us.content
                    cobonee_phone.text = settings.mobile
                    cobonee_email.text = settings.email
                }
                aboutLoading.visibility = View.GONE
                aboutContentLayout.visibility = View.VISIBLE
            }
            is MyUiStates.Error -> {
                activity?.snackBar(state.message, aboutUsRootView)
                aboutLoading.visibility = View.GONE
            }
            MyUiStates.NoConnection -> {
                activity?.snackBar(resources.getString(R.string.no_connection_error), aboutUsRootView)
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
