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
import androidx.navigation.NavController

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
            // Set error state
            GoogleSignInState.setSignInError("Sign-in was cancelled or failed")
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
                            onGoogleSignIn = { launchGoogleSignIn() },
                            onGoogleSignInSuccess = { navController ->
                                Log.d("MainActivityCompose", "Navigating to dashboard after successful sign-in")
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            },
                            onGoogleSignInError = { error ->
                                Log.e("MainActivityCompose", "Google Sign-In error: $error")
                                // Error will be handled in the UI
                            }
                        )
                    }
                }
            }
        }
    }
    
    private fun launchGoogleSignIn() {
        Log.d("MainActivityCompose", "Launching Google Sign-In")
        try {
            val signInIntent = googleSignInHelper.getSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e("MainActivityCompose", "Error launching Google Sign-In", e)
            GoogleSignInState.setSignInError("Failed to launch Google Sign-In: ${e.message}")
        }
    }
    
    private fun handleGoogleSignInResult(data: Intent?) {
        // Handle Google Sign-In result and set it in the shared state
        Log.d("MainActivityCompose", "Handling Google Sign-In result")
        try {
            val account = googleSignInHelper.handleSignInResult(data)
            Log.d("MainActivityCompose", "Account received: ${account?.email}")
            if (account != null) {
                GoogleSignInState.setGoogleSignInAccount(account)
                GoogleSignInState.setSignInSuccess(true)
                Log.d("MainActivityCompose", "Google Sign-In successful, account set")
            } else {
                GoogleSignInState.setSignInError("Sign-in failed: No account received")
            }
        } catch (e: Exception) {
            Log.e("MainActivityCompose", "Error handling Google Sign-In result", e)
            GoogleSignInState.setGoogleSignInAccount(null)
            GoogleSignInState.setSignInError("Sign-in failed: ${e.message}")
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
fun MittiSeApp(
    onGoogleSignIn: () -> Unit,
    onGoogleSignInSuccess: (NavController) -> Unit,
    onGoogleSignInError: (String) -> Unit
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    
    // Navigate to login when user logs out
    LaunchedEffect(authState.isLoggedIn) {
        if (!authState.isLoggedIn && navController.currentDestination?.route == Screen.Dashboard.route) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    // Handle Google Sign-In success
    val googleSignInAccount by GoogleSignInState.googleSignInAccount.collectAsStateWithLifecycle()
    val signInSuccess by GoogleSignInState.signInSuccess.collectAsStateWithLifecycle()
    val signInError by GoogleSignInState.signInError.collectAsStateWithLifecycle()
    
    LaunchedEffect(googleSignInAccount, signInSuccess) {
        if (googleSignInAccount != null && signInSuccess) {
            Log.d("MittiSeApp", "Google Sign-In successful, triggering navigation")
            onGoogleSignInSuccess(navController)
            // Clear the success state to prevent multiple navigations
            GoogleSignInState.clearSignInSuccess()
        }
    }
    
    // Handle Google Sign-In error
    LaunchedEffect(signInError) {
        signInError?.let { error ->
            Log.e("MittiSeApp", "Google Sign-In error: $error")
            onGoogleSignInError(error)
            // Clear the error state
            GoogleSignInState.clearSignInError()
        }
    }
    
    // Determine start destination based on auth state
    val startDestination = if (authState.isLoggedIn) Screen.Dashboard.route else Screen.Login.route
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
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
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                },
                onGoogleSignIn = onGoogleSignIn
            )
        }
        
        composable(Screen.Dashboard.route) {
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