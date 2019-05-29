package com.cobonee.app.ui.auth.loginActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.ui.auth.forgetPassword.sendEmail.SendEmailActivity
import com.cobonee.app.ui.auth.registerActivity.RegisterActivity
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.*
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    var callbackManager: CallbackManager? = null
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cobonee.app.R.layout.activity_login)
        if (Injector.getPreferenceHelper().language.equals(Constants.Language.ARABIC.value)) {
            Injector.getApplicationContext().changeLanguage(Constants.Language.ARABIC)
        } else {
            Injector.getApplicationContext().changeLanguage(Constants.Language.ENGLISH)
        }
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.loginUiState.observe(this, Observer { onLoginResponse(it) })
        viewModel.saveUserUI.observe(this, Observer { onUserSaved(it) })


        register_btn.setOnClickListener {
            RegisterActivity.start(this)
        }

        login_btn.setOnClickListener {
            viewModel.login(edit_email.text.toString(), edit_pass.text.toString())
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

        forget_pass.setOnClickListener {
            SendEmailActivity.start(this)
        }

        //face login

        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()

        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if(isLoggedIn){
            LoginManager.getInstance().logOut()
        }

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                if(isLoggedIn){
                    viewModel.loginFac(accessToken.token)
                }else{
                }
            }
            override fun onCancel() {
            }
            override fun onError(exception: FacebookException) {
            }
        })

        login_facebook_btn.setOnClickListener {
            login_button.performClick()
        }

    }

    private fun onUserSaved(state: MyUiStates?) {
        when (state) {
            MyUiStates.Success -> {
                toast(resources.getString(com.cobonee.app.R.string.login_success))
                loginLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
                HomeActivity.start(this)
                finish()
            }
            is MyUiStates.Error -> {
                snackBar(state.message, loginRootView)
            }
            null -> {
            }
        }
    }

    private fun onLoginResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                loginLoading.visibility = View.VISIBLE
                edit_email.visibility = View.INVISIBLE
                edit_pass.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {
                viewModel.saveUser()
            }
            is MyUiStates.Error -> {
                snackBar(state.message, loginRootView)
                loginLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                snackBar(resources.getString(com.cobonee.app.R.string.no_connection_error), loginRootView)
                loginLoading.visibility = View.GONE
                edit_email.visibility = View.VISIBLE
                edit_pass.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}