package com.ibrahim.home

import com.ibrahim.currencyconverter.di.AppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import retrofit2.Retrofit

@Module(
    includes = [
        AppModule::class
    ]
)
@InstallIn(FragmentComponent::class)
object HomeNetworkModule {

    @Provides
    fun provideHomeRemoteDataSource(retrofit: Retrofit): IHomeRemoteDataSource {
        return retrofit.create(IHomeRemoteDataSource::class.java)
    }

}