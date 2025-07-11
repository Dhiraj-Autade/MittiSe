package com.example.mittise.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: FirebaseUser? = null,
    val userInfo: Map<String, Any>? = null,
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // User logged out, clear the profile data
            _uiState.value = ProfileUiState()
        } else {
            // User logged in or still logged in, load profile
            loadUserProfile()
        }
    }
    
    init {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
        loadUserProfile()
    }
    
    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
    
    private fun loadUserProfile() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(
                user = currentUser,
                isLoading = true
            )
            
            viewModelScope.launch {
                authRepository.getUserInfo(currentUser.uid)
                    .onSuccess { userInfo ->
                        _uiState.value = _uiState.value.copy(
                            userInfo = userInfo,
                            isLoading = false
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message
                        )
                    }
            }
        }
    }
    
    fun showLogoutDialog() {
        _uiState.value = _uiState.value.copy(showLogoutDialog = true)
    }
    
    fun hideLogoutDialog() {
        _uiState.value = _uiState.value.copy(showLogoutDialog = false)
    }
    
    fun logout(): Boolean {
        return try {
            authRepository.signOut()
            _uiState.value = _uiState.value.copy(
                user = null,
                userInfo = null,
                showLogoutDialog = false
            )
            true
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to logout: ${e.message}",
                showLogoutDialog = false
            )
            false
        }
    }
    
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun getDisplayName(): String {
        val userInfo = _uiState.value.userInfo
        val user = _uiState.value.user
        
        // If no user is logged in, return guest user
        if (user == null) {
            return "Guest User"
        }
        
        return when {
            userInfo != null -> {
                val firstName = userInfo["firstName"] as? String ?: ""
                val lastName = userInfo["lastName"] as? String ?: ""
                "$firstName $lastName".trim()
            }
            user.displayName?.isNotBlank() == true -> user.displayName!!
            user.email?.isNotBlank() == true -> user.email!!.substringBefore("@")
            else -> "Unknown User"
        }
    }
    
    fun getFirstName(): String {
        val userInfo = _uiState.value.userInfo
        return userInfo?.get("firstName") as? String ?: "User"
    }
}
