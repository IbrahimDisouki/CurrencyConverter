package com.ibrahim.currencyconverter.exchangerate

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ExchangeRateData(
    val base: String,
    val amount: Double = 1.0,
    val target: String,
    val rate: Double
) : Parcelable