package com.example.mittise.di

import com.example.mittise.data.api.MarketplaceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // This will be replaced with actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMarketplaceApi(): MarketplaceApi {
        return com.example.mittise.data.api.FakeMarketplaceApi()
    }

//    @Provides
//    @Singleton
//    fun provideMarketplaceApi(retrofit: Retrofit): MarketplaceApi {
//        return retrofit.create(MarketplaceApi::class.java)
//    }
} 