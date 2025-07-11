package com.example.mittise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mittise.navigation.MittiSeMainApp
import com.example.mittise.navigation.Screen
import com.example.mittise.ui.screens.LoginScreen
import com.example.mittise.ui.screens.SignUpScreen
import com.example.mittise.ui.theme.MittiSeTheme
import com.example.mittise.ui.viewmodels.AuthViewModel
import com.example.mittise.util.GoogleSignInHelper
import com.example.mittise.util.GoogleSignInState
import com.example.mittise.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivityCompose : ComponentActivity() {
    
    private lateinit var googleSignInHelper: GoogleSignInHelper
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("MainActivityCompose", "Google Sign-In result received: ${result.resultCode}")
        if (result.resultCode == RESULT_OK) {
            // Handle Google Sign-In result
            handleGoogleSignInResult(result.data)
        } else {
            Log.e("MainActivityCompose", "Google Sign-In failed with result code: ${result.resultCode}")
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Google Sign-In helper
        googleSignInHelper = GoogleSignInHelper(this)
        
        // Apply saved language before setting content
        LocaleHelper.onAttach(this)
        
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setContent {
            MittiSeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MittiSeApp(
                            onGoogleSignIn = { launchGoogleSignIn() }
                        )
                    }
                }
            }
        }
    }
    
    private fun launchGoogleSignIn() {
        Log.d("MainActivityCompose", "Launching Google Sign-In")
        val signInIntent = googleSignInHelper.getSignInIntent()
        googleSignInLauncher.launch(signInIntent)
    }
    
    private fun handleGoogleSignInResult(data: Intent?) {
        // Handle Google Sign-In result and set it in the shared state
        Log.d("MainActivityCompose", "Handling Google Sign-In result")
        try {
            val account = googleSignInHelper.handleSignInResult(data)
            Log.d("MainActivityCompose", "Account received: ${account?.email}")
            GoogleSignInState.setGoogleSignInAccount(account)
        } catch (e: Exception) {
            Log.e("MainActivityCompose", "Error handling Google Sign-In result", e)
            GoogleSignInState.setGoogleSignInAccount(null)
        }
    }
    
    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }
    
    override fun onResume() {
        super.onResume()
        // Ensure locale is applied when resuming
        LocaleHelper.onAttach(this)
    }
}

@Composable
fun MittiSeApp(onGoogleSignIn: () -> Unit) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    
    // Navigate to login when user logs out
    LaunchedEffect(authState.isLoggedIn) {
        if (!authState.isLoggedIn && navController.currentDestination?.route == Screen.Main.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    // Determine start destination based on auth state
    val startDestination = if (authState.isLoggedIn) Screen.Main.route else Screen.Login.route
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onGoogleSignIn = onGoogleSignIn
            )
        }
        
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                onGoogleSignIn = onGoogleSignIn
            )
        }
        
        composable(Screen.Main.route) {
            MittiSeMainApp(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
} 