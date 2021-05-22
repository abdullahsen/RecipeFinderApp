package com.abdullahsen.recipefinderapp.di.module

import com.abdullahsen.recipefinderapp.data.remote.RandomRecipeAPI
import com.abdullahsen.recipefinderapp.utils.Constants
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

    //It provides retrofit instance
    @Provides
    @Singleton
    fun provideRetrofit(
    ): Retrofit =
        Retrofit.Builder()
            // Configure retrofit to parse JSON
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()

    //It generates an implementation of the PropertyApiService interface.
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RandomRecipeAPI =
        retrofit.create(RandomRecipeAPI::class.java)

}