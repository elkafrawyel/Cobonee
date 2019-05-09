package com.cobonee.app.ui.main.settingsFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.cobonee.app.R
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.changeLanguage
import kotlinx.android.synthetic.main.settings_fragment.*
import org.intellij.lang.annotations.Language


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentLocale = resources.configuration.locale.language
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.language_array,
            R.layout.spinner_item_black
        )
            .also { adapter ->
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                spinner_language.adapter = adapter
            }
        if (currentLocale == "ar") {
            spinner_language.setSelection(1)

        } else {
            spinner_language.setSelection(0)
        }

        spinner_language?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 1 && currentLocale != "ar") {
                    Injector.getPreferenceHelper().language = Constants.Language.ARABIC.value
                    activity?.changeLanguage(Constants.Language.ARABIC)
                    activity?.finish()
                    HomeActivity.start(requireContext());
                } else if (position == 0 && currentLocale != "en") {
                    Injector.getPreferenceHelper().language = Constants.Language.ENGLISH.value
                    activity?.changeLanguage(Constants.Language.ENGLISH)
                    activity?.finish()
                    HomeActivity.start(requireContext());
                }
            }

        }

    }

}
