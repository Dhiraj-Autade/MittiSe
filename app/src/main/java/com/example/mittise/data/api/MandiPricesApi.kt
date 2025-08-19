package com.example.mittise.data.api

import android.util.Log
import com.example.mittise.data.model.MandiPrice
import com.example.mittise.data.model.MandiApiResponse
import com.example.mittise.data.model.MandiApiRecord
import retrofit2.http.GET
import retrofit2.http.Query

interface MandiPricesApi {
    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070")
    suspend fun getMandiPrices(
        @Query("api-key") apiKey: String = "579b464db66ec23bdd0000016c8c15c073f94fc1608add75486d243f",
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("filters") filters: String? = null,
        @Query("select") select: String? = null,
        @Query("order") order: String? = null
    ): MandiApiResponse
} 