package com.cobonee.app.ui.main.profileFragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.lifecycle.Observer

import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        mainViewModel.citiesUiState.observe(this, Observer { onCitiesResponse(it) })
        mainViewModel.getCities()

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_array,
            R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            profileGender.adapter = adapter
        }

        setUserData()

        mainInfoChanges()

        extraInfoChanges()

        profileEditMainInfoTv.setOnClickListener {
            if (viewModel.canEditMain) {
                viewModel.canEditMain = false
                profileEditMainInfoTv.text = Injector.getApplicationContext().resources.getString(R.string.text_edit)
                profileCancelMainInfoTv.visibility = View.GONE
            } else {
                viewModel.canEditMain = true
                profileEditMainInfoTv.text = Injector.getApplicationContext().resources.getString(R.string.text_save)
                profileCancelMainInfoTv.visibility = View.VISIBLE
            }
            mainInfoChanges()

        }

        profileEditExtraInfoTv.setOnClickListener {
            if (viewModel.canEditExtra) {
                viewModel.canEditExtra = false
                profileEditExtraInfoTv.text = Injector.getApplicationContext().resources.getString(R.string.text_edit)
                profileCancelExtraInfoTv.visibility = View.GONE
            } else {
                viewModel.canEditExtra = true
                profileEditExtraInfoTv.text = Injector.getApplicationContext().resources.getString(R.string.text_save)
                profileCancelExtraInfoTv.visibility = View.VISIBLE
            }
            extraInfoChanges()
        }

        profileCancelMainInfoTv.setOnClickListener {
            viewModel.canEditMain = false
            profileEditMainInfoTv.text = Injector.getApplicationContext().resources.getString(R.string.text_edit)
            profileCancelMainInfoTv.visibility = View.GONE
            mainInfoChanges()
            setUserData()
        }

        profileCancelExtraInfoTv.setOnClickListener {
            viewModel.canEditExtra = false
            profileEditExtraInfoTv.text = Injector.getApplicationContext().resources.getString(R.string.text_edit)
            profileCancelExtraInfoTv.visibility = View.GONE
            extraInfoChanges()
            setUserData()
        }
    }


    private fun setUserData() {
        val user = viewModel.getUserData()
        profileUserName.setText(user.name)
        profileEmail.setText(user.email)
        profileCitySpinner.setSelection(0)
        profileGender.setSelection(0)
        profileCountry.setText("")
        profilePhone.setText(user.mobile)
    }

    private fun onCitiesResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                profilePb.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                profilePb.visibility = View.GONE

                val citiesAdapter =
                    ArrayAdapter<City>(context!!, R.layout.simple_spinner_dropdown_item, mainViewModel.citiesList)
                citiesAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                profileCitySpinner.adapter = citiesAdapter
            }
            is MyUiStates.Error -> {
                profilePb.visibility = View.GONE
                activity?.snackBar(resources.getString(R.string.error_general), profileRootView)
            }
            MyUiStates.NoConnection -> {
                profilePb.visibility = View.GONE
                activity?.snackBar(resources.getString(R.string.no_connection_error), profileRootView)
            }
            null -> {
            }
        }
    }


    private fun mainInfoChanges() {
        if (viewModel.canEditMain) {
            profileUserName.isEnabled = true
            profileEmail.isEnabled = true
            profileCancelMainInfoTv.visibility = View.VISIBLE

        } else {
            profileUserName.isEnabled = false
            profileEmail.isEnabled = false
            profileCancelMainInfoTv.visibility = View.GONE

        }
    }

    private fun extraInfoChanges() {
        if (viewModel.canEditExtra) {
            profileGender.isEnabled = true
            profileCountry.isEnabled = true
            profileCitySpinner.isEnabled = true
            profilePhone.isEnabled = true
            profileCancelExtraInfoTv.visibility = View.VISIBLE

        } else {
            profileGender.isEnabled = false
            profileCountry.isEnabled = false
            profileCitySpinner.isEnabled = false
            profilePhone.isEnabled = false
            profileCancelExtraInfoTv.visibility = View.GONE

        }
    }

}
