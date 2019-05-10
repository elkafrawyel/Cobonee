package com.cobonee.app

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.cobonee.app.utily.Constants
import com.cobonee.app.utily.Injector
import com.cobonee.app.utily.changeLanguage
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Utils.init(this)

        applyLanguage()
    }


    fun applyLanguage(){
        if (Injector.getPreferenceHelper().language.equals(Constants.Language.ARABIC.value)) {
            changeLanguage(Constants.Language.ARABIC)
        } else {
            changeLanguage(Constants.Language.ENGLISH)
        }
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}