package com.cobonee.app.ui.auth.forgetPassword.sendCode

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cobonee.app.R

class SendCodeActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SendCodeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_code)
    }
}
