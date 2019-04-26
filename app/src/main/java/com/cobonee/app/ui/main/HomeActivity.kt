package com.cobonee.app.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.StartElementListener
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.cobonee.app.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        menuImgv.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

        searchImgv.setOnClickListener { searchClicked() }

        cartImgv.setOnClickListener { cartClicked() }

        navigationView.setNavigationItemSelectedListener(this)

    }

    private fun cartClicked() {
        findNavController(R.id.fragment).navigate(R.id.cartFragment)
    }

    private fun searchClicked() {
        findNavController(R.id.fragment).navigate(R.id.searchFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

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
        findNavController(R.id.fragment).navigate(R.id.homeFragment)
    }

    private fun Context.toast(message :String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}
