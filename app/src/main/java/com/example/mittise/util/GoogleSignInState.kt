package com.example.mittise.util

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GoogleSignInState {
    private val _googleSignInAccount = MutableStateFlow<GoogleSignInAccount?>(null)
    val googleSignInAccount: StateFlow<GoogleSignInAccount?> = _googleSignInAccount.asStateFlow()
    
    private val _signInSuccess = MutableStateFlow<Boolean>(false)
    val signInSuccess: StateFlow<Boolean> = _signInSuccess.asStateFlow()
    
    private val _signInError = MutableStateFlow<String?>(null)
    val signInError: StateFlow<String?> = _signInError.asStateFlow()
    
    fun setGoogleSignInAccount(account: GoogleSignInAccount?) {
        _googleSignInAccount.value = account
    }
    
    fun setSignInSuccess(success: Boolean) {
        _signInSuccess.value = success
    }
    
    fun setSignInError(error: String?) {
        _signInError.value = error
    }
    
    fun clearGoogleSignInAccount() {
        _googleSignInAccount.value = null
    }
    
    fun clearSignInSuccess() {
        _signInSuccess.value = false
    }
    
    fun clearSignInError() {
        _signInError.value = null
    }
    
    fun clearAll() {
        _googleSignInAccount.value = null
        _signInSuccess.value = false
        _signInError.value = null
    }
} 