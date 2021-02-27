package com.ibrahim.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibrahim.currencyconverter.di.BaseViewModelFactory
import com.ibrahim.currencyconverter.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@DisableInstallInCheck
abstract class HomeModule {
    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory

    @Binds
    abstract fun bindHomeRepository(repository: HomeRepository): IHomeRepository
}