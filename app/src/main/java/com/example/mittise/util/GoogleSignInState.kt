package com.example.mittise.util

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GoogleSignInState {
    private val _googleSignInAccount = MutableStateFlow<GoogleSignInAccount?>(null)
    val googleSignInAccount: StateFlow<GoogleSignInAccount?> = _googleSignInAccount.asStateFlow()
    
    fun setGoogleSignInAccount(account: GoogleSignInAccount?) {
        _googleSignInAccount.value = account
    }
    
    fun clearGoogleSignInAccount() {
        _googleSignInAccount.value = null
    }
} 