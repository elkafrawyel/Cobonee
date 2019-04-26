package com.cobonee.app.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.StartElementListener
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.cobonee.app.R
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        menuImgv.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        searchImgv.setOnClickListener { searchClicked() }

        cartImgv.setOnClickListener { cartClicked() }

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.getItem(0).isChecked = true;
    }

    private fun cartClicked() {
        findNavController(R.id.fragment).navigate(R.id.cartFragment)
    }

    private fun searchClicked() {
        findNavController(R.id.fragment).navigate(R.id.searchFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.menu.getItem(0).isChecked = false
        item.isCheckable = true;
        item.isChecked = true;
        when(item.itemId){
            R.id.nav_deals ->   findNavController(R.id.fragment).navigate(R.id.homeFragment)
            R.id.nav_cart ->    findNavController(R.id.fragment).navigate(R.id.cartFragment)
            R.id.nav_saved->    findNavController(R.id.fragment).navigate(R.id.savedFragment)
            R.id.nav_orders->   findNavController(R.id.fragment).navigate(R.id.ordersFragment)
            R.id.nav_magazine-> findNavController(R.id.fragment).navigate(R.id.magazineFragment)
            R.id.nav_profile -> findNavController(R.id.fragment).navigate(R.id.profileFragment)
            R.id.nav_aboutUs->  findNavController(R.id.fragment).navigate(R.id.aboutUsFragment)
            R.id.nav_settings-> findNavController(R.id.fragment).navigate(R.id.settingsFragment)
            R.id.nav_logout->   logout()
            R.id.nav_help->     findNavController(R.id.fragment).navigate(R.id.helpFragment)
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        toast("Logout Done")
        snackBar("Logout Done",rootView)
        LoginActivity.start(this)
        findNavController(R.id.fragment).navigate(R.id.homeFragment)
    }

}
