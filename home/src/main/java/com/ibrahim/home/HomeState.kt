package com.ibrahim.home

import com.ibrahim.core.CoreState
import com.ibrahim.core.Failure

sealed class HomeState : CoreState {
    object Idle : HomeState()
    object Loading : HomeState()
    data class ExchangeRateSuccess(val exchangeRates: ExchangeRates) : HomeState()
    data class ExchangeRateFailure(val failure: Failure) : HomeState()
}