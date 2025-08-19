package com.example.mittise.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mittise.data.repository.AuthRepository
import com.example.mittise.data.repository.ProfileRepository
import com.example.mittise.util.NetworkUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class ProfileUiState(
    val user: FirebaseUser? = null,
    val userInfo: Map<String, Any>? = null,
    val isLoading: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isEditing: Boolean = false,
    val showImageSourceDialog: Boolean = false,
    val isUploadingImage: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    private val storage = FirebaseStorage.getInstance()
    
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
                        if (userInfo == null) {
                            // User document doesn't exist, create it
                            val defaultUserInfo = createDefaultUserInfo(currentUser)
                            authRepository.updateUserInfo(currentUser.uid, defaultUserInfo)
                                .onSuccess {
                                    _uiState.value = _uiState.value.copy(
                                        userInfo = defaultUserInfo,
                                        isLoading = false
                                    )
                                }
                                .onFailure { exception ->
                                    _uiState.value = _uiState.value.copy(
                                        isLoading = false,
                                        errorMessage = "Failed to create user profile: ${exception.message}"
                                    )
                                }
                        } else {
                            // Clean the user info if it contains error messages
                            val cleanedUserInfo = cleanUserInfo(userInfo)
                            _uiState.value = _uiState.value.copy(
                                userInfo = cleanedUserInfo,
                                isLoading = false
                            )
                        }
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
    
    private fun cleanUserInfo(userInfo: Map<String, Any>): Map<String, Any> {
        return userInfo.toMutableMap().apply {
            // Clean bio field if it contains error messages
            val bio = this["bio"] as? String
            if (bio != null && (bio.contains("NOT_FOUND") || bio.contains("Firestore document"))) {
                this["bio"] = ""
            }
        }
    }
    
    private fun createDefaultUserInfo(user: FirebaseUser): Map<String, Any> {
        return mapOf(
            "firstName" to (user.displayName?.split(" ")?.firstOrNull() ?: ""),
            "lastName" to (user.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: ""),
            "email" to (user.email ?: ""),
            "phone" to "",
            "location" to "",
            "bio" to "",
            "yearsExperience" to "",
            "farmSize" to "",
            "cropsGrown" to "",
            "profileImageUrl" to (user.photoUrl?.toString() ?: ""),
            "createdAt" to System.currentTimeMillis(),
            "updatedAt" to System.currentTimeMillis()
        )
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
    
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
    
    fun startEditing() {
        _uiState.value = _uiState.value.copy(isEditing = true)
    }
    
    fun stopEditing() {
        _uiState.value = _uiState.value.copy(isEditing = false)
    }
    
    fun updateProfile(
        firstName: String,
        lastName: String,
        phone: String,
        location: String,
        bio: String? = null,
        yearsExperience: String? = null,
        farmSize: String? = null,
        cropsGrown: String? = null,
        profileImageUrl: String? = null
    ) {
        val currentUser = _uiState.value.user
        if (currentUser == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No user logged in"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Get existing user info or create default
            val existingUserInfo = _uiState.value.userInfo ?: createDefaultUserInfo(currentUser)
            
            val updatedUserInfo = hashMapOf<String, Any>().apply {
                putAll(existingUserInfo)
                put("firstName", firstName)
                put("lastName", lastName)
                put("phone", phone)
                put("location", location)
                put("updatedAt", System.currentTimeMillis())
                
                // Handle bio field properly - only update if it's not empty and not an error message
                if (bio != null && bio.isNotEmpty() && !bio.contains("NOT_FOUND") && !bio.contains("Firestore document")) {
                    put("bio", bio)
                }
                if (yearsExperience != null && yearsExperience.isNotEmpty()) put("yearsExperience", yearsExperience)
                if (farmSize != null && farmSize.isNotEmpty()) put("farmSize", farmSize)
                if (cropsGrown != null && cropsGrown.isNotEmpty()) put("cropsGrown", cropsGrown)
                if (profileImageUrl != null && profileImageUrl.isNotEmpty()) put("profileImageUrl", profileImageUrl)
            }
            
            authRepository.updateUserInfo(currentUser.uid, updatedUserInfo)
                .onSuccess {
                    // Update the UI state with the new user info immediately
                    _uiState.value = _uiState.value.copy(
                        userInfo = updatedUserInfo,
                        isLoading = false,
                        isEditing = false,
                        successMessage = "Profile updated successfully! Redirecting..."
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to update profile: ${exception.message}"
                    )
                }
        }
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
    
    fun getBio(): String {
        val userInfo = _uiState.value.userInfo
        return userInfo?.get("bio") as? String ?: "Experienced farmer with 5+ years in sustainable agriculture. Passionate about organic farming and helping fellow farmers."
    }
    
    fun getYearsExperience(): String {
        val userInfo = _uiState.value.userInfo
        return userInfo?.get("yearsExperience") as? String ?: "5"
    }
    
    fun getFarmSize(): String {
        val userInfo = _uiState.value.userInfo
        return userInfo?.get("farmSize") as? String ?: "10"
    }
    
    fun getCropsGrown(): String {
        val userInfo = _uiState.value.userInfo
        return userInfo?.get("cropsGrown") as? String ?: "Wheat, Rice, Cotton"
    }
    
    fun getJoinDate(): String {
        val userInfo = _uiState.value.userInfo
        val createdAt = userInfo?.get("createdAt") as? Long
        return if (createdAt != null) {
            val date = java.util.Date(createdAt)
            val formatter = java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault())
            formatter.format(date)
        } else {
            "Recently"
        }
    }
    
    fun getProfileImageUrl(): String? {
        val userInfo = _uiState.value.userInfo
        return userInfo?.get("profileImageUrl") as? String
    }
    
    fun showImageSourceDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceDialog = true)
    }
    
    fun hideImageSourceDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceDialog = false)
    }
    
    fun resetFormState() {
        // Reset any form-related state if needed
        _uiState.value = _uiState.value.copy(
            isEditing = false,
            showImageSourceDialog = false,
            isUploadingImage = false
        )
    }
    
    fun refreshProfile() {
        loadUserProfile()
    }
    
    // Upload profile image to Firebase Storage
    suspend fun uploadProfileImage(imageUri: Uri): Result<String> {
        val currentUser = _uiState.value.user ?: return Result.failure(Exception("No user logged in"))
        
        return try {
            _uiState.value = _uiState.value.copy(isUploadingImage = true)
            
            // Create a reference to store the image
            val imageRef = storage.reference
                .child("profile_images")
                .child("${currentUser.uid}_${System.currentTimeMillis()}.jpg")
            
            // Upload the image
            val uploadTask = imageRef.putFile(imageUri).await()
            
            // Get the download URL
            val downloadUrl = imageRef.downloadUrl.await()
            
            _uiState.value = _uiState.value.copy(isUploadingImage = false)
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isUploadingImage = false,
                errorMessage = "Failed to upload image: ${e.message}"
            )
            Result.failure(e)
        }
    }
    
    // Enhanced update profile method that handles image upload
    fun updateProfileWithImage(
        firstName: String,
        lastName: String,
        phone: String,
        location: String,
        bio: String? = null,
        yearsExperience: String? = null,
        farmSize: String? = null,
        cropsGrown: String? = null,
        imageUri: Uri? = null
    ) {
        // Check network connectivity first
        if (!networkUtil.isNetworkAvailable()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No network connection. Please check your internet connection and try again."
            )
            return
        }

        viewModelScope.launch {
            try {
                var profileImageUrl: String? = null
                
                // Upload image if provided
                if (imageUri != null) {
                    uploadProfileImage(imageUri)
                        .onSuccess { url -> profileImageUrl = url }
                        .onFailure { exception ->
                            _uiState.value = _uiState.value.copy(
                                errorMessage = "Failed to upload image: ${exception.message}"
                            )
                            return@launch
                        }
                }
                
                // Update profile with or without new image
                updateProfile(
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    location = location,
                    bio = bio,
                    yearsExperience = yearsExperience,
                    farmSize = farmSize,
                    cropsGrown = cropsGrown,
                    profileImageUrl = profileImageUrl
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update profile: ${e.message}"
                )
            }
        }
    }
}
