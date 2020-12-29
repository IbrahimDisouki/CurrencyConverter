package com.ibrahim.home

import androidx.fragment.app.Fragment
import com.ibrahim.currencyconverter.di.AppDependencies
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@Singleton
@Component(dependencies = [AppDependencies::class], modules = [HomeModule::class])
interface HomeComponent {

    @ExperimentalCoroutinesApi
    fun inject(fragment: HomeFragment)

    fun fragment(): Fragment

    @Component.Factory
    interface Factory {
        fun homeComponent(
            @BindsInstance fragment: Fragment,
            appDependencies: AppDependencies
        ): HomeComponent
    }

}