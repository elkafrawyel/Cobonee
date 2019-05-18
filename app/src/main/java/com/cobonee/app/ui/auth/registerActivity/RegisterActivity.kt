package com.cobonee.app.ui.auth.registerActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.ui.auth.loginActivity.LoginViewModel
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Injector.getPreferenceHelper().language.equals(Constants.Language.ARABIC.value)) {
            Injector.getApplicationContext().changeLanguage(Constants.Language.ARABIC)
        } else {
            Injector.getApplicationContext().changeLanguage(Constants.Language.ENGLISH)
        }

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModel.registerUiState.observe(this, Observer { onRegisterResponse(it) })
        viewModel.saveUserUI.observe(this, Observer { onUserSaved(it) })
        viewModel.citiesUiState.observe(this, Observer { onCityResponse(it) })

        viewModel.getCities()
        registerCitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                viewModel.selectedCity.value = viewModel.citiesList[position]
            }
        }

        login_btn.setOnClickListener {
            LoginActivity.start(this)
            finish()
        }

        register_btn.setOnClickListener {
            if (edit_pass.text.toString() != edit_pass_confirm.text.toString()) {
                snackBar(resources.getString(R.string.no_password_match), registerRootView)
            } else {
                var cityId = (registerCitySpinner.selectedItem as City).id
                viewModel.register(
                    edit_name.text.toString(),
                    edit_email.text.toString(),
                    edit_pass.text.toString(),
                    cityId.toString()
                )

            }
        }

        val currentLocale = resources.configuration.locale.language

        change_language.setOnClickListener {
            if (currentLocale == "ar") {
                //change to english
                changeLanguage(Constants.Language.ENGLISH)
                saveLanguage(Constants.Language.ENGLISH)
            } else {
                //change to arabic
                changeLanguage(Constants.Language.ARABIC)
                saveLanguage(Constants.Language.ARABIC)
            }
            restartApplication()
        }
    }

    private fun onCityResponse(states: MyUiStates?) {
        when (states) {
            MyUiStates.Loading -> {
                registerLoading.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                registerLoading.visibility = View.GONE
                val citiesAdapter =
                    ArrayAdapter<City>(this@RegisterActivity, R.layout.simple_spinner_item, viewModel.citiesList)
                citiesAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                registerCitySpinner.adapter = citiesAdapter
            }
            is MyUiStates.Error -> {
                snackBar(states.message, registerRootView)
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), registerRootView)
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            null -> {

            }
        }
    }

    private fun onUserSaved(state: MyUiStates?) {
        when (state) {
            MyUiStates.Success -> {
                toast(resources.getString(R.string.register_success))
                registerLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
                HomeActivity.start(this)
            }
            is MyUiStates.Error -> {
                snackBar(state.message, registerRootView)
            }
            null -> {
            }
        }
    }

    private fun onRegisterResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                registerLoading.visibility = View.VISIBLE
                editsLayout.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {
                viewModel.saveUser()
            }
            is MyUiStates.Error -> {
                snackBar(state.message, registerRootView)
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), registerRootView)
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }
}