package com.example.mittise.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FarmerProduct(
    val id: String,
    val productName: String,
    val productType: String, // Fruit, Vegetable, Grain, etc.
    val quantity: Double,
    val unit: String, // kg, ton, etc.
    val expectedPrice: Double,
    val priceUnit: String, // per kg, per ton, etc.
    val location: String,
    val farmerName: String,
    val farmerContact: String,
    val description: String,
    val imageUrl: String?,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable 