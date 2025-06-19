package com.example.mittise.data.api

import com.example.mittise.data.model.Product

class FakeMarketplaceApi : MarketplaceApi {
    override suspend fun getProducts(
        category: String?,
        location: String?,
        page: Int,
        limit: Int
    ): List<Product> {
        return listOf(
            Product(
                id = "1",
                name = "Wheat",
                description = "High quality wheat",
                price = 2000.0,
                unit = "quintal",
                quantity = 10.0,
                imageUrl = "",
                sellerId = "seller1",
                sellerName = "Farmer Ram",
                location = "Delhi",
                category = "Grains",
                rating = 4.5f,
                isVerified = true,
                createdAt = System.currentTimeMillis()
            ),
            Product(
                id = "2",
                name = "Rice",
                description = "Premium basmati rice",
                price = 3000.0,
                unit = "quintal",
                quantity = 5.0,
                imageUrl = "",
                sellerId = "seller2",
                sellerName = "Farmer Shyam",
                location = "Punjab",
                category = "Grains",
                rating = 4.0f,
                isVerified = false,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getProductDetails(productId: String): Product {
        return getProducts().first()
    }

    override suspend fun getCategories(): List<String> {
        return listOf("Grains", "Vegetables", "Fruits")
    }

    override suspend fun getLocations(): List<String> {
        return listOf("Delhi", "Punjab", "Haryana")
    }
} 