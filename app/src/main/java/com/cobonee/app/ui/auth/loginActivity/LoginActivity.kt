package com.cobonee.app.ui.auth.loginActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.cobonee.app.R
import com.cobonee.app.ui.auth.registerActivity.RegisterActivity
import com.cobonee.app.ui.main.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register_btn.setOnClickListener {
            RegisterActivity.start(this)
        }
    }
}
