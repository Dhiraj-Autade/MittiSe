package com.example.mittise.data.model

import com.google.gson.annotations.SerializedName

data class MandiApiResponse(
    @SerializedName("records")
    val records: List<MandiApiRecord>? = null,
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("status")
    val status: String? = null
)

data class MandiApiRecord(
    @SerializedName("commodity")
    val commodity: String? = null,
    @SerializedName("variety")
    val variety: String? = null,
    @SerializedName("market")
    val market: String? = null,
    @SerializedName("market_code")
    val marketCode: String? = null,
    @SerializedName("district")
    val district: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("min_price")
    val minPrice: String? = null,
    @SerializedName("max_price")
    val maxPrice: String? = null,
    @SerializedName("modal_price")
    val modalPrice: String? = null,
    @SerializedName("price_unit")
    val priceUnit: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("arrival_quantity")
    val arrivalQuantity: String? = null,
    @SerializedName("arrival_unit")
    val arrivalUnit: String? = null,
    @SerializedName("source")
    val source: String? = null
) {
    fun toMandiPrice(): MandiPrice {
        return MandiPrice(
            commodityName = commodity ?: "",
            commodityVariety = variety ?: "",
            marketName = market ?: "",
            marketCode = marketCode ?: "",
            district = district ?: "",
            state = state ?: "",
            minPrice = minPrice?.toDoubleOrNull() ?: 0.0,
            maxPrice = maxPrice?.toDoubleOrNull() ?: 0.0,
            modalPrice = modalPrice?.toDoubleOrNull() ?: 0.0,
            priceUnit = priceUnit ?: "₹ per Quintal",
            date = date ?: "",
            arrivalQuantity = arrivalQuantity?.toDoubleOrNull() ?: 0.0,
            arrivalUnit = arrivalUnit ?: "Quintal",
            source = source ?: "Agmarknet"
        )
    }
} 