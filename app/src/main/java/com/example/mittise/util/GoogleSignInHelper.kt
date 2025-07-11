package com.example.mittise.util

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class GoogleSignInHelper(private val context: Context) {
    
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    
    private val googleSignInClient: GoogleSignInClient by lazy {
        Log.d("GoogleSignInHelper", "Initializing Google Sign-In with Web Client ID: ${GoogleSignInConfig.WEB_CLIENT_ID}")
        
        val gso = if (GoogleSignInConfig.USE_FALLBACK) {
            // Use basic Google Sign-In without Firebase integration for testing
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        } else {
            // Use Firebase integration
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GoogleSignInConfig.WEB_CLIENT_ID)
                .requestEmail()
                .build()
        }
        
        GoogleSignIn.getClient(context, gso)
    }
    
    fun getSignInIntent(): Intent {
        Log.d("GoogleSignInHelper", "Getting sign-in intent")
        return googleSignInClient.signInIntent
    }
    
    fun handleSignInResult(data: Intent?): com.google.android.gms.auth.api.signin.GoogleSignInAccount? {
        Log.d("GoogleSignInHelper", "Handling sign-in result")
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            // Use blocking call for simplicity, or you can use callbacks
            val account = task.getResult(ApiException::class.java)
            Log.d("GoogleSignInHelper", "Sign-in successful: ${account?.email}")
            account
        } catch (e: ApiException) {
            Log.e("GoogleSignInHelper", "Sign-in failed with status: ${e.statusCode}", e)
            null
        }
    }
    
    fun signOut() {
        googleSignInClient.signOut()
        firebaseAuth.signOut()
    }
    
    fun getLastSignedInAccount(): com.google.android.gms.auth.api.signin.GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
} 