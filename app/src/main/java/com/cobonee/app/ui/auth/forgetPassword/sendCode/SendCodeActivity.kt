package com.cobonee.app.ui.auth.forgetPassword.sendCode

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cobonee.app.R
import com.cobonee.app.ui.auth.forgetPassword.ForgetViewModel
import com.cobonee.app.ui.auth.forgetPassword.NewPasswordActivity.NewPasswordActivity
import com.cobonee.app.utily.MyUiStates
import com.cobonee.app.utily.snackBar
import kotlinx.android.synthetic.main.activity_send_code.*

class SendCodeActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SendCodeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var viewModel: ForgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_code)

        viewModel = ViewModelProviders.of(this).get(ForgetViewModel::class.java)
        viewModel.checkUiState.observe(this, Observer { onForgetResponse(it)  })

        sendCode.setOnClickListener {
            val email =intent.getStringExtra("email")
            viewModel.check(email,code1.text.toString())
        }
    }
    private fun onForgetResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                forgetLoading.visibility = View.VISIBLE
                code1.visibility = View.INVISIBLE
            }
            MyUiStates.Success -> {

                val intent = Intent(this@SendCodeActivity,NewPasswordActivity::class.java)
                intent.putExtra("token",viewModel.newToken)
                startActivity(intent)
            }
            is MyUiStates.Error -> {
                snackBar(state.message, forgetRootView)
                forgetLoading.visibility = View.GONE
                code1.visibility = View.VISIBLE
            }
            MyUiStates.NoConnection -> {
                snackBar(resources.getString(R.string.no_connection_error), forgetRootView)
                forgetLoading.visibility = View.GONE
                code1.visibility = View.VISIBLE
            }
            null -> {
            }
        }
    }
}
