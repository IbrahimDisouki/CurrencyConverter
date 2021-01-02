package com.ibrahim.currencyconverter.application

import android.app.Application
import com.ibrahim.currencyconverter.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

import timber.log.Timber.DebugTree

@HiltAndroidApp
class CurrencyConverterApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

}