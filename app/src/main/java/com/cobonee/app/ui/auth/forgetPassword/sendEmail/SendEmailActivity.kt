package com.cobonee.app.ui.auth.forgetPassword.sendEmail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.ui.auth.forgetPassword.ForgetViewModel
import com.cobonee.app.ui.auth.forgetPassword.sendCode.SendCodeActivity
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.activity_send_email.*
import kotlinx.android.synthetic.main.activity_send_email.loginLoading

class SendEmailActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SendEmailActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: ForgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_email)

        viewModel = ViewModelProviders.of(this).get(ForgetViewModel::class.java)
        viewModel.forgetUiState.observe(this, Observer { onForgetResponse(it) })

        confirmEmail.setOnClickListener {
            viewModel.forget(emailEt.text.toString())
        }


    }private fun onForgetResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                loginLoading.visibility = View.VISIBLE
                emailEt.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {
                SendCodeActivity.start(this)
            }
            is MyUiStates.Error -> {
                snackBar(state.message, forgetRootView)
                loginLoading.visibility = View.GONE
                emailEt.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), forgetRootView)
                loginLoading.visibility = View.GONE
                emailEt.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }
}
