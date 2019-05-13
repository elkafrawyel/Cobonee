package com.cobonee.app.utily

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.cobonee.app.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun Context.changeLanguage(lang: Constants.Language) {
    val locale = Locale(lang.value)
    Locale.setDefault(locale)
    val config = this.resources.configuration
    config.setLocale(locale)
    this.createConfigurationContext(config)
    this.resources.updateConfiguration(config, this.resources.displayMetrics)
}

fun saveLanguage(language: Constants.Language) {
    if (language == Constants.Language.ARABIC)
        Injector.getPreferenceHelper().language = "ar"
    else
        Injector.getPreferenceHelper().language = "en"

}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBar(message: String, rootView: View) {
    val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.show()
}

fun Context.restartApplication() {
    val intent = Injector.getApplicationContext().packageManager.getLaunchIntentForPackage(
        Injector.getApplicationContext().packageName
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
}