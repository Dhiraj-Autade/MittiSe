package com.example.mittise.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    
    // Real-time profile data observer
    fun observeUserProfile(userId: String): Flow<Map<String, Any>?> = callbackFlow {
        val listener = firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val data = snapshot?.data
                trySend(data)
            }
        
        awaitClose { listener.remove() }
    }
    
    // Upload profile image
    suspend fun uploadProfileImage(userId: String, imageUri: Uri): Result<String> {
        return try {
            val imageRef = storage.reference
                .child("profile_images")
                .child("${userId}_${System.currentTimeMillis()}.jpg")
            
            val uploadTask = imageRef.putFile(imageUri).await()
            val downloadUrl = imageRef.downloadUrl.await()
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Update profile data
    suspend fun updateProfile(userId: String, profileData: Map<String, Any>): Result<Unit> {
        return try {
            val updatedData = profileData.toMutableMap().apply {
                put("updatedAt", System.currentTimeMillis())
            }
            
            firestore.collection("users")
                .document(userId)
                .set(updatedData, SetOptions.merge())
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Get user statistics
    suspend fun getUserStatistics(userId: String): Result<Map<String, Any>> {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val userData = userDoc.data ?: emptyMap()
            
            // Calculate profile completion percentage
            val requiredFields = listOf("firstName", "lastName", "phone", "location", "bio")
            val completedFields = requiredFields.count { field ->
                val value = userData[field] as? String
                value != null && value.isNotBlank()
            }
            val completionPercentage = (completedFields.toFloat() / requiredFields.size * 100).toInt()
            
            val statistics = mapOf(
                "profileCompletion" to completionPercentage,
                "joinDate" to (userData["createdAt"] ?: System.currentTimeMillis()),
                "lastUpdated" to (userData["updatedAt"] ?: userData["createdAt"] ?: System.currentTimeMillis())
            )
            
            Result.success(statistics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Delete profile image
    suspend fun deleteProfileImage(imageUrl: String): Result<Unit> {
        return try {
            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Backup profile data
    suspend fun backupProfileData(userId: String): Result<Unit> {
        return try {
            val userDoc = firestore.collection("users").document(userId).get().await()
            val userData = userDoc.data
            
            if (userData != null) {
                // Create a backup in a separate collection
                val backupData = userData.toMutableMap().apply {
                    put("backupTimestamp", System.currentTimeMillis())
                    put("originalUserId", userId)
                }
                
                firestore.collection("user_backups")
                    .document("${userId}_${System.currentTimeMillis()}")
                    .set(backupData)
                    .await()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
