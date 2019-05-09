package com.cobonee.app.ui.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cobonee.app.R
import com.cobonee.app.ui.main.HomeActivity
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.changeLanguage

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        if (Injector.getPreferenceHelper().language.equals(Constants.Language.ARABIC.value)){
            changeLanguage(Constants.Language.ARABIC)
        }else{
            changeLanguage(Constants.Language.ENGLISH)
        }
        HomeActivity.start(this)
        finish()
    }
}
