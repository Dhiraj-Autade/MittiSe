package com.example.mittise.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val unit: String,
    val quantity: Double,
    val imageUrl: String,
    val sellerId: String,
    val sellerName: String,
    val location: String,
    val category: String,
    val rating: Float,
    val isVerified: Boolean,
    val createdAt: Long
) : Parcelable 