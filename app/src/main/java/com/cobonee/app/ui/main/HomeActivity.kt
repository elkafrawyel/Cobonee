package com.cobonee.app.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.cobonee.app.R
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.ui.auth.registerActivity.RegisterActivity
import com.cobonee.app.utily.snackBar
import com.cobonee.app.utily.toast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*


const val HOME_INDEX = 0
const val CART_INDEX = 1
const val SAVED_INDEX = 2
const val ORDERS_INDEX = 3
const val MAGAZINE_INDEX = 4
const val PROFILE_INDEX = 5
const val ABOUT_US_INDEX = 6
const val SETTINGS_INDEX = 7
const val LOGOUT_INDEX = 8
const val HELP_INDEX = 9

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

        searchImgv.setOnClickListener { searchClicked() }

        cartImgv.setOnClickListener { cartClicked() }

        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        setupActionBarWithNavController(this, findNavController(R.id.fragment), drawerLayout);

        setupWithNavController(navigationView, findNavController(R.id.fragment));

        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.getItem(HOME_INDEX).isChecked = true
        onNavigationDestinationChanged()
        findNavController(R.id.fragment).navigate(R.id.knetFragment)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

    private fun cartClicked() {
        findNavController(R.id.fragment).navigate(R.id.cartFragment)
    }

    private fun searchClicked() {
        findNavController(R.id.fragment).navigate(R.id.searchFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true;

        drawerLayout.closeDrawers();

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

        return true
    }

    private fun onNavigationDestinationChanged() {
        findNavController(R.id.fragment).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    setHomeTitle(resources.getString(R.string.app_name))
                    searchImgv.visibility = View.VISIBLE
                    cartImgv.visibility = View.VISIBLE
                    cartNumberTv.visibility = View.VISIBLE
                    navigationView.menu.getItem(HOME_INDEX).isChecked = true
                    navigationView.menu.getItem(HOME_INDEX).isCheckable = true
                }
                R.id.cartFragment -> {
                    setHomeTitle(resources.getString(R.string.label_cart))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(CART_INDEX).isChecked = true
                    navigationView.menu.getItem(CART_INDEX).isCheckable = true
                }
                R.id.savedFragment -> {
                    setHomeTitle(resources.getString(R.string.label_saved))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(SAVED_INDEX).isChecked = true
                    navigationView.menu.getItem(SAVED_INDEX).isCheckable = true
                }
                R.id.ordersFragment -> {
                    setHomeTitle(resources.getString(R.string.label_orders))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    navigationView.menu.getItem(ORDERS_INDEX).isChecked = true
                    navigationView.menu.getItem(ORDERS_INDEX).isCheckable = true
                }
                R.id.magazineFragment -> {
                    setHomeTitle(resources.getString(R.string.label_magazine))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(MAGAZINE_INDEX).isChecked = true
                    navigationView.menu.getItem(MAGAZINE_INDEX).isCheckable = true
                }
                R.id.profileFragment -> {
                    setHomeTitle(resources.getString(R.string.label_profile))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(PROFILE_INDEX).isChecked = true
                    navigationView.menu.getItem(PROFILE_INDEX).isCheckable = true
                }
                R.id.aboutUsFragment -> {
                    setHomeTitle(resources.getString(R.string.label_info))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(ABOUT_US_INDEX).isChecked = true
                    navigationView.menu.getItem(ABOUT_US_INDEX).isCheckable = true
                }
                R.id.settingsFragment -> {
                    setHomeTitle(resources.getString(R.string.label_settings))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(SETTINGS_INDEX).isChecked = true
                    navigationView.menu.getItem(SETTINGS_INDEX).isCheckable = true
                }
                R.id.helpFragment -> {
                    setHomeTitle(resources.getString(R.string.label_help))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                    navigationView.menu.getItem(HELP_INDEX).isChecked = true
                    navigationView.menu.getItem(HELP_INDEX).isCheckable = true
                }

                R.id.detailsFragment -> {
                    setHomeTitle(resources.getString(R.string.lable_offer_details))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.VISIBLE
                    cartNumberTv.visibility = View.VISIBLE
                }

                R.id.paymentFragment-> {
                    setHomeTitle(resources.getString(R.string.lable_payment))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                }

                R.id.visaFragment-> {
                    setHomeTitle(resources.getString(R.string.lable_visa))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                }

                R.id.knetFragment-> {
                    setHomeTitle(resources.getString(R.string.lable_knet))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                }

                R.id.searchFragment-> {
                    setHomeTitle(resources.getString(R.string.lable_search))
                    searchImgv.visibility = View.GONE
                    cartImgv.visibility = View.GONE
                    cartNumberTv.visibility = View.GONE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (findNavController(R.id.fragment).currentDestination?.id == R.id.homeFragment) {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun logout() {
        toast("Logout Done")
        findNavController(R.id.fragment).navigate(R.id.homeFragment)
        LoginActivity.start(this);
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (findNavController(R.id.fragment).currentDestination?.id == R.id.homeFragment) {
                finish()
            }else if(findNavController(R.id.fragment).currentDestination?.id == R.id.paymentFragment){
                findNavController(R.id.fragment).navigate(R.id.homeFragment)
            }else{
                super.onBackPressed()
            }
        }
    }

    fun setHomeTitle(title: String) {
        homeTitleTv.text = title
    }

}
