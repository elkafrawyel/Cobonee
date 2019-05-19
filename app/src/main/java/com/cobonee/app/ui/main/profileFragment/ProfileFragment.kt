package com.cobonee.app.ui.main.profileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.entity.UpdateProfileBody
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.ui.main.MainViewModel
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import kotlinx.android.synthetic.main.activity_register.*
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
        return inflater.inflate(com.cobonee.app.R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.updateProfileUiState.observe(this, Observer { onCitiesResponse(it) })
        mainViewModel.citiesUiState.observe(this, Observer { onCitiesResponse(it) })
        mainViewModel.getCities()

        ArrayAdapter.createFromResource(
            requireContext(),
            com.cobonee.app.R.array.gender_array,
            com.cobonee.app.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(com.cobonee.app.R.layout.simple_spinner_dropdown_item)
            profileGender.adapter = adapter
        }

        setUserData()

        mainInfoChanges()

        extraInfoChanges()

        profileEditMainInfoTv.setOnClickListener {
            if(mainViewModel.citiesList.size==0)return@setOnClickListener
            if (viewModel.canEditMain) {
                viewModel.canEditMain = false
                profileEditMainInfoTv.text = Injector.getApplicationContext().resources.getString(com.cobonee.app.R.string.text_edit)
                profileCancelMainInfoTv.visibility = View.GONE
                updateUserData()
            } else {
                viewModel.canEditMain = true
                profileEditMainInfoTv.text = Injector.getApplicationContext().resources.getString(com.cobonee.app.R.string.text_save)
                profileCancelMainInfoTv.visibility = View.VISIBLE
            }
            mainInfoChanges()

        }

        profileEditExtraInfoTv.setOnClickListener {
            if(mainViewModel.citiesList.size==0)return@setOnClickListener
            if (viewModel.canEditExtra) {
                viewModel.canEditExtra = false
                profileEditExtraInfoTv.text = Injector.getApplicationContext().resources.getString(com.cobonee.app.R.string.text_edit)
                profileCancelExtraInfoTv.visibility = View.GONE
                updateUserData()
            } else {
                viewModel.canEditExtra = true
                profileEditExtraInfoTv.text = Injector.getApplicationContext().resources.getString(com.cobonee.app.R.string.text_save)
                profileCancelExtraInfoTv.visibility = View.VISIBLE
            }
            extraInfoChanges()
        }

        profileCancelMainInfoTv.setOnClickListener {
            viewModel.canEditMain = false
            profileEditMainInfoTv.text = Injector.getApplicationContext().resources.getString(com.cobonee.app.R.string.text_edit)
            profileCancelMainInfoTv.visibility = View.GONE
            mainInfoChanges()
            setUserData()
        }

        profileCancelExtraInfoTv.setOnClickListener {
            viewModel.canEditExtra = false
            profileEditExtraInfoTv.text = Injector.getApplicationContext().resources.getString(com.cobonee.app.R.string.text_edit)
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

    private fun updateUserData(){
        val genderArray = resources.getStringArray(com.cobonee.app.R.array.gender_array)
        viewModel.updateProfile(
            UpdateProfileBody(profileUserName.text.toString(),
                mainViewModel.citiesList[profileCitySpinner.selectedItemPosition].id,
                profilePhone.text.toString(),
                genderArray[profileGender.selectedItemPosition])
        )
    }

    private fun onCitiesResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                profilePb.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                profilePb.visibility = View.GONE

                val citiesAdapter =
                    ArrayAdapter<City>(context!!, com.cobonee.app.R.layout.simple_spinner_dropdown_item, mainViewModel.citiesList)
                citiesAdapter.setDropDownViewResource(com.cobonee.app.R.layout.simple_spinner_dropdown_item)
                profileCitySpinner.adapter = citiesAdapter
            }
            is MyUiStates.Error -> {
                profilePb.visibility = View.GONE
                activity?.snackBar(state.message, profileRootView)
                setUserData()
            }
            MyUiStates.NoConnection -> {
                profilePb.visibility = View.GONE
                activity?.snackBar(resources.getString(com.cobonee.app.R.string.no_connection_error), profileRootView)
                setUserData()
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
