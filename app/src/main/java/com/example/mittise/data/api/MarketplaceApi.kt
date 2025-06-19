package com.example.mittise.data.api

import com.example.mittise.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarketplaceApi {
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("location") location: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): List<Product>

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") productId: String): Product

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/locations")
    suspend fun getLocations(): List<String>
} 