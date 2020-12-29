package com.ibrahim.home

import com.ibrahim.core.CoreResult
import com.ibrahim.core.Failure

sealed class HomeResult : CoreResult {
    object Loading : HomeResult()
    data class ExchangeRateSuccess(val exchangeRates: ExchangeRates) : HomeResult()
    data class ExchangeRateFailure(val failure: Failure) : HomeResult()
}