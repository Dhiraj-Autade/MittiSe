package com.example.mittise.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    
    val isUserLoggedIn: Boolean
        get() = currentUser != null
    
    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            
            if (user != null) {
                // Update user profile with display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName("$firstName $lastName")
                    .build()
                user.updateProfile(profileUpdates).await()
                
                // Store additional user info in Firestore
                val userInfo = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to email,
                    "createdAt" to System.currentTimeMillis()
                )
                
                firestore.collection("users")
                    .document(user.uid)
                    .set(userInfo)
                    .await()
                
                Result.success(user)
            } else {
                Result.failure(Exception("User creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user
            
            if (user != null) {
                // Ensure user document exists in Firestore
                ensureUserDocumentExists(user)
                Result.success(user)
            } else {
                Result.failure(Exception("Sign in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun signOut() {
        firebaseAuth.signOut()
    }
    
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            // Check if we have an ID token for Firebase authentication
            if (account.idToken != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val user = authResult.user
                
                if (user != null) {
                    // Check if user exists in Firestore, if not create user document
                    val userDoc = firestore.collection("users").document(user.uid).get().await()
                    
                    if (!userDoc.exists()) {
                        // Create new user document for Google Sign-In
                        val userInfo = hashMapOf(
                            "firstName" to (account.givenName ?: ""),
                            "lastName" to (account.familyName ?: ""),
                            "email" to (account.email ?: ""),
                            "createdAt" to System.currentTimeMillis(),
                            "signInMethod" to "google"
                        )
                        
                        firestore.collection("users")
                            .document(user.uid)
                            .set(userInfo)
                            .await()
                    }
                    
                    Result.success(user)
                } else {
                    Result.failure(Exception("Google sign in failed"))
                }
            } else {
                // For testing without Firebase integration, create a mock user
                // In production, you should always have an ID token
                Result.failure(Exception("ID token is required for Firebase authentication"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserInfo(userId: String): Result<Map<String, Any>?> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            if (document.exists()) {
                Result.success(document.data)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateUserInfo(userId: String, userInfo: Map<String, Any>): Result<Unit> {
        return try {
            println("Updating user info for userId: $userId")
            println("User info: $userInfo")
            
            // Add timestamp for when the profile was last updated
            val updatedUserInfo = userInfo.toMutableMap().apply {
                put("updatedAt", System.currentTimeMillis())
            }
            
            firestore.collection("users")
                .document(userId)
                .set(updatedUserInfo, SetOptions.merge())
                .await()
            
            println("User info updated successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            println("Failed to update user info: ${e.message}")
            Result.failure(e)
        }
    }
    
    // Add method to check if user profile is complete
    suspend fun isProfileComplete(userId: String): Result<Boolean> {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            if (document.exists()) {
                val data = document.data
                val requiredFields = listOf("firstName", "lastName", "phone", "location")
                val isComplete = requiredFields.all { field ->
                    val value = data?.get(field) as? String
                    value != null && value.isNotBlank()
                }
                Result.success(isComplete)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun ensureUserDocumentExists(user: FirebaseUser) {
        try {
            println("Checking if user document exists for userId: ${user.uid}")
            
            val document = firestore.collection("users")
                .document(user.uid)
                .get()
                .await()
            
            if (!document.exists()) {
                println("User document doesn't exist, creating it...")
                // Create basic user document if it doesn't exist
                val userInfo = hashMapOf(
                    "firstName" to (user.displayName?.split(" ")?.firstOrNull() ?: ""),
                    "lastName" to (user.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: ""),
                    "email" to (user.email ?: ""),
                    "createdAt" to System.currentTimeMillis()
                )
                
                firestore.collection("users")
                    .document(user.uid)
                    .set(userInfo)
                    .await()
                
                println("User document created successfully")
            } else {
                println("User document already exists")
            }
        } catch (e: Exception) {
            // Log error but don't fail the sign in process
            println("Failed to ensure user document exists: ${e.message}")
        }
    }
}
