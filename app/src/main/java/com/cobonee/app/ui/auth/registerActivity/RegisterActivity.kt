package com.cobonee.app.ui.auth.registerActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.entity.Data
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.changeLanguage
import com.cobonee.app.utily.snackBar
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

        login_btn.setOnClickListener {
            LoginActivity.start(this)
        }

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModel.registerUiState.observe(this, Observer { onRegisterResponse(it) })


        register_btn.setOnClickListener {
            if(!edit_pass.text.toString().equals(edit_pass_confirm.text.toString())){
                snackBar(resources.getString(R.string.no_password_match), registerRootView)
            }else{
                viewModel.getRegister(edit_name.text.toString(),edit_email.text.toString(),edit_pass.text.toString())
            }
        }

    }

    private fun onRegisterResponse(state: RegisterViewModel.RegisterUiState?) {
        when (state) {
            RegisterViewModel.RegisterUiState.Loading -> {
                registerLoading.visibility = View.VISIBLE
                editsLayout.visibility = View.INVISIBLE
            }
            RegisterViewModel.RegisterUiState.Success -> {
                var registerResponse: LoginResponse? = viewModel.registerResponse
                if(registerResponse?.message!=null){
                    //register success
                    var userData :Data = registerResponse.data
                    snackBar("Login success", registerRootView)
                }
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            is RegisterViewModel.RegisterUiState.Error -> {
                snackBar(state.message, registerRootView)
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            RegisterViewModel.RegisterUiState.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), registerRootView)
                registerLoading.visibility = View.GONE
                editsLayout.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }

}