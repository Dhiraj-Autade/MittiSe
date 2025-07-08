package com.example.mittise.data.repository

import com.example.mittise.data.api.MarketplaceApi
import com.example.mittise.data.model.FarmerProduct
import com.example.mittise.data.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketplaceRepository @Inject constructor(
    private val api: MarketplaceApi
) {
    suspend fun getProducts(category: String? = null, location: String? = null): List<Product> {
        return api.getProducts(category, location)
    }

    suspend fun getFarmerProducts(category: String? = null, location: String? = null): List<FarmerProduct> {
        return api.getFarmerProducts(category, location)
    }

    suspend fun getCategories(): List<String> {
        return api.getCategories()
    }

    suspend fun getLocations(): List<String> {
        return api.getLocations()
    }
}
