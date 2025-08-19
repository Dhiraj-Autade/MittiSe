package com.example.mittise.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
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
            
            firestore.collection("users")
                .document(userId)
                .set(userInfo, com.google.firebase.firestore.SetOptions.merge())
                .await()
            
            println("User info updated successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            println("Failed to update user info: ${e.message}")
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
