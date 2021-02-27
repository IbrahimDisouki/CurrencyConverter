package com.ibrahim.home

import android.content.Context
import com.ibrahim.currencyconverter.di.AppDependencies
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AppDependencies::class],
    modules = [HomeModule::class, HomeNetworkModule::class]
)
interface HomeComponent {

    @ExperimentalCoroutinesApi
    fun inject(fragment: HomeFragment)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(appDependencies: AppDependencies): Builder
        fun build(): HomeComponent
    }

}