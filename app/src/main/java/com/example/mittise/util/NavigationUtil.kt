package com.example.mittise.util

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.mittise.navigation.Screen

object NavigationUtil {
    private const val TAG = "NavigationUtil"
    
    fun navigateToMainScreen(navController: NavController, route: String) {
        try {
            Log.d(TAG, "navigateToMainScreen: Navigating to $route")
            
            // Special handling for Dashboard navigation
            if (route == Screen.Dashboard.route) {
                // For Dashboard, pop everything and navigate fresh
                navController.navigate(route) {
                    popUpTo(Screen.Dashboard.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            } else {
                // For other main screens, clear back to Dashboard
                navController.navigate(route) {
                    popUpTo(Screen.Dashboard.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            
            Log.d(TAG, "navigateToMainScreen: Navigation successful to $route")
        } catch (e: Exception) {
            Log.e(TAG, "navigateToMainScreen: Error navigating to $route", e)
            // Fallback: simple navigation
            navController.navigate(route)
        }
    }
    
    fun navigateToScreen(navController: NavController, route: String) {
        try {
            Log.d(TAG, "navigateToScreen: Navigating to $route")
            navController.navigate(route) {
                launchSingleTop = true
            }
            Log.d(TAG, "navigateToScreen: Navigation successful to $route")
        } catch (e: Exception) {
            Log.e(TAG, "navigateToScreen: Error navigating to $route", e)
            // Fallback: simple navigation  
            navController.navigate(route)
        }
    }
    
    fun isMainBottomNavScreen(route: String): Boolean {
        return route in listOf(
            Screen.Dashboard.route,
            Screen.Marketplace.route,
            Screen.Apmc.route,
            Screen.Social.route,
            Screen.Profile.route
        )
    }
    
    fun navigateWithBottomNav(navController: NavController, route: String, currentRoute: String?) {
        Log.d(TAG, "navigateWithBottomNav: From $currentRoute to $route")
        
        // Don't navigate if already on the same route
        if (currentRoute == route) {
            Log.d(TAG, "navigateWithBottomNav: Already on $route, skipping navigation")
            return
        }
        
        try {
            if (isMainBottomNavScreen(route)) {
                Log.d(TAG, "navigateWithBottomNav: $route is main screen")
                navigateToMainScreen(navController, route)
            } else {
                Log.d(TAG, "navigateWithBottomNav: $route is secondary screen")
                navigateToScreen(navController, route)
            }
        } catch (e: Exception) {
            Log.e(TAG, "navigateWithBottomNav: Last resort navigation to $route", e)
            // Last resort fallback
            navController.navigate(route)
        }
    }
}
