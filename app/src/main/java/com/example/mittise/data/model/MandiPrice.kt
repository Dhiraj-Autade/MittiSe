package com.example.mittise.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MandiPrice(
    val commodityName: String,
    val commodityVariety: String,
    val marketName: String,
    val marketCode: String,
    val district: String,
    val state: String,
    val minPrice: Double,
    val maxPrice: Double,
    val modalPrice: Double,
    val priceUnit: String,
    val date: String,
    val arrivalQuantity: Double,
    val arrivalUnit: String,
    val source: String
) : Parcelable {
    // Computed properties for UI
    val priceRange: String
        get() = "₹${minPrice.toInt()} - ₹${maxPrice.toInt()}"
    
    val modalPriceFormatted: String
        get() = "₹${modalPrice.toInt()}"
    
    val arrivalFormatted: String
        get() = "${arrivalQuantity.toInt()} ${arrivalUnit}"
    
    val location: String
        get() = "$district, $state"
    
    val priceChange: String
        get() {
            // Mock price change calculation (in real API, this would come from previous day's data)
            val change = (maxPrice - minPrice) / 2
            return if (change > 0) "+₹${change.toInt()}" else "₹${change.toInt()}"
        }
} 