package com.ibrahim.currencyconverter.di

import android.app.Application
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppDependencies {
    fun exposeApplication(): Application
    fun exposeRetrofit(retrofit: Retrofit): Retrofit
}