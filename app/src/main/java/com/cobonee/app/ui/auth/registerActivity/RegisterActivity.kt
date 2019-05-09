package com.cobonee.app.ui.auth.registerActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cobonee.app.R
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.changeLanguage
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Injector.getPreferenceHelper().language.equals(Constants.Language.ARABIC.value)){
            changeLanguage(Constants.Language.ARABIC)
        }else{
            changeLanguage(Constants.Language.ENGLISH)
        }
        login_btn.setOnClickListener {
            LoginActivity.start(this)
        }
    }
}
