package com.example.mittise

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mittise.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up the bottom navigation
        binding.navView.setupWithNavController(navController)

        // Set up the drawer navigation
        binding.navViewDrawer.setNavigationItemSelectedListener(this)

        // Configure the app bar
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard,
                R.id.navigation_marketplace,
                R.id.navigation_apmc,
                R.id.navigation_social,
                R.id.navigation_profile
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        // Add navigation listener for handling transitions
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Apply 3D transition when navigating between fragments
            supportFragmentManager.fragments.forEach { fragment ->
                fragment.view?.let { view ->
                    view.rotation = 0f
                    view.scaleX = 1f
                    view.scaleY = 1f
                    view.alpha = 1f
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.navigation_dashboard)
            }
            R.id.nav_weather -> {
                navController.navigate(R.id.navigation_weather)
            }
            R.id.nav_crop_calendar -> {
                navController.navigate(R.id.navigation_crop_calendar)
            }
            R.id.nav_soil_test -> {
                navController.navigate(R.id.navigation_soil_testing)
            }
            R.id.nav_help -> {
                navController.navigate(R.id.navigation_help)
            }
            R.id.nav_feedback -> {
                navController.navigate(R.id.navigation_feedback)
            }
            R.id.nav_about -> {
                navController.navigate(R.id.navigation_about)
            }
            R.id.nav_articles -> {
                navController.navigate(R.id.navigation_articles)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
} 