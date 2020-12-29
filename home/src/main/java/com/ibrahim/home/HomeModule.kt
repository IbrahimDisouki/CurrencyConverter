package com.ibrahim.home

import com.ibrahim.currencyconverter.di.AppModule
import com.ibrahim.currencyconverter.di.FragmentViewModelModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import retrofit2.Retrofit

@Module(
    includes = [
        HomeViewModel_HiltModule::class,
        FragmentViewModelModule::class,
        AppModule::class
    ]
)
@InstallIn(FragmentComponent::class)
object HomeModule {

    @Provides
    fun provideHomeRemoteDataSource(retrofit: Retrofit): IHomeRemoteDataSource {
        return retrofit.create(IHomeRemoteDataSource::class.java)
    }

    @Provides
    fun provideHomeRepository(repository: HomeRepository): IHomeRepository {
        return repository
    }

}