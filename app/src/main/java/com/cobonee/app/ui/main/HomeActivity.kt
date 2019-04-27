package com.cobonee.app.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.cobonee.app.R
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*

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
        navigationView.menu.getItem(0).isCheckable = true;

        onNavigationDestinationChanged()

//        findNavController(R.id.fragment).navigate(R.id.questionsFragment)
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
        when (item.itemId) {
            R.id.nav_deals -> {
                findNavController(R.id.fragment).navigate(R.id.homeFragment)
            }
            R.id.nav_cart -> {
                findNavController(R.id.fragment).navigate(R.id.cartFragment)
            }
            R.id.nav_saved -> {
                findNavController(R.id.fragment).navigate(R.id.savedFragment)
            }
            R.id.nav_orders -> {
                findNavController(R.id.fragment).navigate(R.id.ordersFragment)
            }
            R.id.nav_magazine -> {
                findNavController(R.id.fragment).navigate(R.id.magazineFragment)
            }
            R.id.nav_profile -> {
                findNavController(R.id.fragment).navigate(R.id.profileFragment)
            }
            R.id.nav_aboutUs -> {
                findNavController(R.id.fragment).navigate(R.id.aboutUsFragment)
            }
            R.id.nav_settings -> {
                findNavController(R.id.fragment).navigate(R.id.settingsFragment)
            }
            R.id.nav_logout -> logout()
            R.id.nav_help -> {
                findNavController(R.id.fragment).navigate(R.id.helpFragment)
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun onNavigationDestinationChanged() {
        findNavController(R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.homeFragment -> {
                    setHomeTitle(resources.getString(R.string.app_name))
                    searchImgv.visibility = View.VISIBLE
                    cartImgv.visibility = View.VISIBLE
                }
                R.id.cartFragment -> {
                    setHomeTitle(resources.getString(R.string.label_cart))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.savedFragment -> {
                    setHomeTitle(resources.getString(R.string.label_saved))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.ordersFragment -> {
                    setHomeTitle(resources.getString(R.string.label_orders))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.magazineFragment -> {
                    setHomeTitle(resources.getString(R.string.label_magazine))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.profileFragment -> {
                    setHomeTitle(resources.getString(R.string.label_profile))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.aboutUsFragment -> {
                    setHomeTitle(resources.getString(R.string.label_info))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.settingsFragment -> {
                    setHomeTitle(resources.getString(R.string.label_settings))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
                R.id.helpFragment -> {
                    setHomeTitle(resources.getString(R.string.label_help))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                }
            }
        }
    }

    private fun logout() {
        toast("Logout Done")
        LoginActivity.start(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed()
        }
    }

    public fun setHomeTitle(title: String) {
        homeTitleTv.text = title
    }

}
