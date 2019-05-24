package com.cobonee.app.ui.auth.forgetPassword.NewPasswordActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.ui.auth.forgetPassword.ForgetViewModel
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.activity_new_password.*

class NewPasswordActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NewPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: ForgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)
        viewModel = ViewModelProviders.of(this).get(ForgetViewModel::class.java)
        viewModel.resetUiState.observe(this, Observer { onForgetResponse(it)  })
        viewModel.saveUserUI.observe(this, Observer { onUserSaved(it) })

        resetEmail.setOnClickListener {
            val token =intent.getStringExtra("token")
            viewModel.reset(token,passwordEt.text.toString(),confirmPasswordEt.text.toString())
        }
    }
    private fun onForgetResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                forgetLoading.visibility = View.VISIBLE
                passwordEt.visibility = View.INVISIBLE
                confirmPasswordEt.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {
                viewModel.saveUser()
            }
            is MyUiStates.Error -> {
                snackBar(state.message, forgetRootView)
                forgetLoading.visibility = View.GONE
                passwordEt.visibility = View.VISIBLE
                confirmPasswordEt.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), forgetRootView)
                forgetLoading.visibility = View.GONE
                passwordEt.visibility = View.VISIBLE
                confirmPasswordEt.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }

    private fun onUserSaved(state: MyUiStates?) {
        when (state) {
            MyUiStates.Success -> {
                forgetLoading.visibility = View.GONE
                HomeActivity.start(this)
            }
            is MyUiStates.Error -> {
                snackBar(state.message, forgetRootView)
            }
            null -> {
            }
        }
    }
}
