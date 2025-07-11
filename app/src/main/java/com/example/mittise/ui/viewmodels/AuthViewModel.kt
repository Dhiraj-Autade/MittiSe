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

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val user: FirebaseUser? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        val currentUser = auth.currentUser
        _uiState.value = _uiState.value.copy(
            isLoggedIn = currentUser != null,
            user = currentUser
        )
    }
    
    init {
        // Add auth state listener to monitor changes
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
        checkCurrentUser()
    }
    
    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
    
    private fun checkCurrentUser() {
        val currentUser = authRepository.currentUser
        _uiState.value = _uiState.value.copy(
            isLoggedIn = currentUser != null,
            user = currentUser
        )
    }
    
    fun refreshAuthState() {
        checkCurrentUser()
    }
    
    fun signUp(firstName: String, lastName: String, email: String, password: String) {
        if (!validateSignUpInput(firstName, lastName, email, password)) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            authRepository.signUp(firstName, lastName, email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user,
                        successMessage = "Account created successfully!"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
        }
    }
    
    fun signIn(email: String, password: String) {
        if (!validateSignInInput(email, password)) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            authRepository.signIn(email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        user = user,
                        successMessage = "Welcome back!"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
        }
    }
    
    fun signOut() {
        authRepository.signOut()
        _uiState.value = _uiState.value.copy(
            isLoggedIn = false,
            user = null,
            successMessage = "Signed out successfully"
        )
    }
    
    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter your email address")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            authRepository.resetPassword(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Password reset email sent!"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
        }
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
    
    private fun validateSignUpInput(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Boolean {
        when {
            firstName.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter your first name")
                return false
            }
            lastName.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter your last name")
                return false
            }
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter your email")
                return false
            }
            !isValidEmail(email) -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid email address")
                return false
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
                return false
            }
        }
        return true
    }
    
    private fun validateSignInInput(email: String, password: String): Boolean {
        when {
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter your email")
                return false
            }
            password.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter your password")
                return false
            }
        }
        return true
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    private fun getErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("email address is already in use") == true ->
                "This email is already registered. Please use a different email or try signing in."
            exception.message?.contains("password is invalid") == true ||
            exception.message?.contains("wrong-password") == true ->
                "User not found. Please check your credentials and try again."
            exception.message?.contains("user-not-found") == true ||
            exception.message?.contains("no user record") == true ->
                "User not found. Please check your credentials and try again."
            exception.message?.contains("invalid-email") == true ->
                "Invalid email format. Please enter a valid email address."
            exception.message?.contains("user-disabled") == true ->
                "This account has been disabled. Please contact support."
            exception.message?.contains("too-many-requests") == true ->
                "Too many failed attempts. Please try again later."
            exception.message?.contains("network error") == true ->
                "Network error. Please check your internet connection."
            exception.message?.contains("invalid-credential") == true ->
                "User not found. Please check your credentials and try again."
            else -> "User not found. Please check your credentials and try again."
        }
    }
}
