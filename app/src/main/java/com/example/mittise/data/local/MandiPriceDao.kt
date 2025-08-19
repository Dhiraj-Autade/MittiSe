package com.example.mittise.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MandiPriceDao {
    
    @Query("SELECT * FROM mandi_prices ORDER BY timestamp DESC")
    fun getAllPrices(): Flow<List<MandiPriceEntity>>
    
    @Query("SELECT * FROM mandi_prices WHERE state = :state ORDER BY timestamp DESC")
    fun getPricesByState(state: String): Flow<List<MandiPriceEntity>>
    
    @Query("SELECT * FROM mandi_prices WHERE commodityName = :commodity ORDER BY timestamp DESC")
    fun getPricesByCommodity(commodity: String): Flow<List<MandiPriceEntity>>
    
    @Query("SELECT * FROM mandi_prices WHERE marketName = :market ORDER BY timestamp DESC")
    fun getPricesByMarket(market: String): Flow<List<MandiPriceEntity>>
    
    @Query("SELECT * FROM mandi_prices WHERE timestamp > :timestamp ORDER BY timestamp DESC")
    fun getRecentPrices(timestamp: Long): Flow<List<MandiPriceEntity>>
    
    @Query("SELECT DISTINCT state FROM mandi_prices ORDER BY state")
    fun getAllStates(): Flow<List<String>>
    
    @Query("SELECT DISTINCT commodityName FROM mandi_prices ORDER BY commodityName")
    fun getAllCommodities(): Flow<List<String>>
    
    @Query("SELECT DISTINCT marketName FROM mandi_prices ORDER BY marketName")
    fun getAllMarkets(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrices(prices: List<MandiPriceEntity>)
    
    @Query("DELETE FROM mandi_prices WHERE timestamp < :timestamp")
    suspend fun deleteOldPrices(timestamp: Long)
    
    @Query("DELETE FROM mandi_prices")
    suspend fun deleteAllPrices()
    
    @Query("SELECT COUNT(*) FROM mandi_prices")
    suspend fun getPriceCount(): Int
} 