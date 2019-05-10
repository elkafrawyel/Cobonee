package com.cobonee.app.ui.auth.loginActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.entity.LoginResponse
import com.cobonee.app.ui.auth.registerActivity.RegisterActivity
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.changeLanguage
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Injector.getPreferenceHelper().language.equals(Constants.Language.ARABIC.value)) {
            Injector.getApplicationContext().changeLanguage(Constants.Language.ARABIC)
        } else {
            Injector.getApplicationContext().changeLanguage(Constants.Language.ENGLISH)
        }
        register_btn.setOnClickListener {
            RegisterActivity.start(this)
        }

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loginUiState.observe(this, Observer { onLoginResponse(it) })


        login_btn.setOnClickListener {
            viewModel.getLogin(edit_email.text.toString(),edit_pass.text.toString())
        }

    }

    private fun onLoginResponse(state: LoginViewModel.LoginUiState?) {
        when (state) {
            LoginViewModel.LoginUiState.Loading -> {
                loginLoading.visibility = View.VISIBLE
                edit_email.visibility = View.INVISIBLE
                edit_pass.visibility = View.INVISIBLE
            }
            LoginViewModel.LoginUiState.Success -> {
                var loginResponse: LoginResponse? = viewModel.loginResponse
                snackBar("Login success", loginRootView)
                loginLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
            }
            is LoginViewModel.LoginUiState.Error -> {
                snackBar(state.message, loginRootView)
                loginLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
            }
            LoginViewModel.LoginUiState.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), loginRootView)
                loginLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }

}
