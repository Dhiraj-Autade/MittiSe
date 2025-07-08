package com.example.mittise.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Marketplace : Screen("marketplace")
    object Apmc : Screen("apmc")
    object Social : Screen("social")
    object Profile : Screen("profile")
    object Weather : Screen("weather")
    object Advisor : Screen("advisor")
    object Schemes : Screen("schemes")
    object CropCalendar : Screen("crop_calendar")
    object SoilTesting : Screen("soil_testing")
    object Help : Screen("help")
    object Feedback : Screen("feedback")
    object About : Screen("about")
    object Articles : Screen("articles")
    object Language : Screen("language")
    object Settings : Screen("settings")
    object EditProfile : Screen("edit_profile")
    object FarmerProductRegistration : Screen("farmer_product_registration")
    
    object ProductDetails : Screen("product_details/{productId}") {
        val arguments = listOf(
            navArgument("productId") { type = NavType.StringType }
        )
        
        fun createRoute(productId: String) = "product_details/$productId"
    }
    
    object PostDetails : Screen("post_details/{postId}") {
        val arguments = listOf(
            navArgument("postId") { type = NavType.StringType }
        )
        
        fun createRoute(postId: String) = "post_details/$postId"
    }
} 