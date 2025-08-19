package com.example.mittise.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mittise.data.model.MandiPrice

@Entity(tableName = "mandi_prices")
data class MandiPriceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
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
    val source: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toMandiPrice(): MandiPrice {
        return MandiPrice(
            commodityName = commodityName,
            commodityVariety = commodityVariety,
            marketName = marketName,
            marketCode = marketCode,
            district = district,
            state = state,
            minPrice = minPrice,
            maxPrice = maxPrice,
            modalPrice = modalPrice,
            priceUnit = priceUnit,
            date = date,
            arrivalQuantity = arrivalQuantity,
            arrivalUnit = arrivalUnit,
            source = source
        )
    }

    companion object {
        fun fromMandiPrice(mandiPrice: MandiPrice): MandiPriceEntity {
            return MandiPriceEntity(
                commodityName = mandiPrice.commodityName,
                commodityVariety = mandiPrice.commodityVariety,
                marketName = mandiPrice.marketName,
                marketCode = mandiPrice.marketCode,
                district = mandiPrice.district,
                state = mandiPrice.state,
                minPrice = mandiPrice.minPrice,
                maxPrice = mandiPrice.maxPrice,
                modalPrice = mandiPrice.modalPrice,
                priceUnit = mandiPrice.priceUnit,
                date = mandiPrice.date,
                arrivalQuantity = mandiPrice.arrivalQuantity,
                arrivalUnit = mandiPrice.arrivalUnit,
                source = mandiPrice.source
            )
        }
    }
} 