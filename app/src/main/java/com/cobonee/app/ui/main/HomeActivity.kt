package com.cobonee.app.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.cobonee.app.R
import com.cobonee.app.entity.City
import com.cobonee.app.ui.auth.loginActivity.LoginActivity
import com.cobonee.app.ui.auth.registerActivity.RegisterActivity
import com.cobonee.app.utily.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_fragment.*


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

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.citiesUiState.observe(this, Observer { onCitiesResponse(it) })
        viewModel.allCartItemsAddOfferUiState.observe(this, Observer { onCartItemsResponse(it) })

        viewModel.getCities()
        viewModel.getCartItems()

        citiesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectedCity.value = viewModel.citiesList[position]
            }
        }

        searchImgv.setOnClickListener { searchClicked() }

        cartImgv.setOnClickListener { cartClicked() }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupActionBarWithNavController(this, findNavController(R.id.fragment), drawerLayout)

        setupWithNavController(navigationView, findNavController(R.id.fragment))

        navigationView.setNavigationItemSelectedListener(this)
        onNavigationDestinationChanged()

    }

    private fun onCartItemsResponse(states: MyUiStates?) {
        when (states) {
            MyUiStates.Success -> {
                val size = viewModel.cartItems.size
                if (size == 0) {
                    cartNumberTv.visibility = View.GONE
                } else {
                    cartNumberTv.visibility = View.VISIBLE
                    cartNumberTv.text = size.toString()
                }
            }
            is MyUiStates.Error -> {
                cartNumberTv.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        navigationView.menu.getItem(HOME_INDEX).isChecked = true
        navigationView.menu.getItem(HOME_INDEX).isCheckable = true
        if (viewModel.isLoggedIn()) {

            navigationView.menu.getItem(1).isVisible = true
            navigationView.menu.getItem(2).isVisible = true
            navigationView.menu.getItem(3).isVisible = true
            navigationView.menu.getItem(5).isVisible = true
            navigationView.menu.getItem(8).title = resources.getString(R.string.text_logOut)
        } else {
            navigationView.menu.getItem(1).isVisible = false
            navigationView.menu.getItem(2).isVisible = false
            navigationView.menu.getItem(3).isVisible = false
            navigationView.menu.getItem(5).isVisible = false
            navigationView.menu.getItem(8).title = resources.getString(R.string.text_login)
        }

    }

    private fun onCitiesResponse(state: MyUiStates?) {
        when (state) {
            MyUiStates.Loading -> {
                mainPb.visibility = View.VISIBLE
            }
            MyUiStates.Success -> {
                mainPb.visibility = View.GONE
                val citiesAdapter =
                    ArrayAdapter<City>(this@HomeActivity, R.layout.simple_spinner_item, viewModel.citiesList)
                citiesAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                citiesSpinner.adapter = citiesAdapter
            }
            is MyUiStates.Error -> {
                mainPb.visibility = View.GONE
                snackBar(resources.getString(R.string.error_general), rootView)
            }
            MyUiStates.NoConnection -> {
                mainPb.visibility = View.GONE
                snackBar(resources.getString(R.string.no_connection_error), rootView)
            }
            null -> {
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

    private fun cartClicked() {
        findNavController(R.id.fragment).navigate(R.id.cartFragment)
    }

    private fun searchClicked() {
        findNavController(R.id.fragment).navigate(R.id.searchFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true

        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.nav_deals -> {
//                findNavController(R.id.fragment).navigate(R.id.homeFragment)
            }
            R.id.nav_cart -> {
                findNavController(R.id.fragment).navigate(R.id.cartFragment)
            }
            R.id.nav_saved -> {
                findNavController(R.id.fragment).navigate(R.id.favouritesFragment)
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
            R.id.nav_logout -> {
                logout()
            }
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
                    setHomeTitle("")
                    searchImgv.visibility = View.VISIBLE
                    cartImgv.visibility = View.VISIBLE
                    citiesSpinner.visibility = View.VISIBLE
                    navigationView.menu.getItem(HOME_INDEX).isChecked = true
                    navigationView.menu.getItem(HOME_INDEX).isCheckable = true
                }
                R.id.cartFragment -> {
                    setHomeTitle(resources.getString(R.string.label_cart))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(CART_INDEX).isChecked = true
                    navigationView.menu.getItem(CART_INDEX).isCheckable = true
                }
                R.id.favouritesFragment -> {
                    setHomeTitle(resources.getString(R.string.label_saved))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(SAVED_INDEX).isChecked = true
                    navigationView.menu.getItem(SAVED_INDEX).isCheckable = true
                }
                R.id.ordersFragment -> {
                    setHomeTitle(resources.getString(R.string.label_orders))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(ORDERS_INDEX).isChecked = true
                    navigationView.menu.getItem(ORDERS_INDEX).isCheckable = true
                }
                R.id.magazineFragment -> {
                    setHomeTitle(resources.getString(R.string.label_magazine))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(MAGAZINE_INDEX).isChecked = true
                    navigationView.menu.getItem(MAGAZINE_INDEX).isCheckable = true
                }
                R.id.profileFragment -> {
                    setHomeTitle(resources.getString(R.string.label_profile))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(PROFILE_INDEX).isChecked = true
                    navigationView.menu.getItem(PROFILE_INDEX).isCheckable = true
                }
                R.id.aboutUsFragment -> {
                    setHomeTitle(resources.getString(R.string.label_info))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(ABOUT_US_INDEX).isChecked = true
                    navigationView.menu.getItem(ABOUT_US_INDEX).isCheckable = true
                }
                R.id.settingsFragment -> {
                    setHomeTitle(resources.getString(R.string.label_settings))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(SETTINGS_INDEX).isChecked = true
                    navigationView.menu.getItem(SETTINGS_INDEX).isCheckable = true
                }
                R.id.helpFragment -> {
                    setHomeTitle(resources.getString(R.string.label_help))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    navigationView.menu.getItem(HELP_INDEX).isChecked = true
                    navigationView.menu.getItem(HELP_INDEX).isCheckable = true
                }

                R.id.detailsFragment -> {
                    setHomeTitle(resources.getString(R.string.lable_offer_details))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.VISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                }

                R.id.paymentFragment -> {
                    setHomeTitle(resources.getString(R.string.lable_payment))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                }

                R.id.visaFragment -> {
                    setHomeTitle(resources.getString(R.string.lable_visa))
                    searchImgv.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                }

                R.id.knetFragment -> {
                    setHomeTitle(resources.getString(R.string.lable_knet))
                    searchImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                }

                R.id.searchFragment -> {
                    setHomeTitle(resources.getString(R.string.lable_search))
                    searchImgv.visibility = View.INVISIBLE
                    citiesSpinner.visibility = View.INVISIBLE
                    cartImgv.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        if (viewModel.isLoggedIn()) {
            //Logout
            viewModel.logOut()

        } else {
            //login

        }

        navigationView.menu.getItem(HOME_INDEX).isChecked = true
        navigationView.menu.getItem(HOME_INDEX).isCheckable = true
        navigationView.menu.getItem(LOGOUT_INDEX).isChecked = false
        navigationView.menu.getItem(LOGOUT_INDEX).isCheckable = false
        LoginActivity.start(this)
    }

    fun homeBackClicked(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
         finish()
        }
    }
    fun setHomeTitle(title: String) {
        homeTitleTv.text = title
    }

}
