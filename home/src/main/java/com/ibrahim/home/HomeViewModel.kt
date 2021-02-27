package com.ibrahim.home

import com.ibrahim.core.CoreViewModel
import com.ibrahim.core.None
import com.ibrahim.core.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeViewModel @Inject constructor(
    private val latestExchangeRateUseCase: LatestExchangeRateUseCase
) :
    CoreViewModel<HomeIntent, HomeResult, HomeState>(HomeState.Idle) {

    override suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetLatestExchangeRate -> {
                latestExchangeRateUseCase.execute(None()).collect {
                    mutableState.value = reduce(it)
                }
            }
        }.exhaustive
    }

    override fun reduce(result: HomeResult): HomeState {
        return when (result) {
            is HomeResult.Loading -> {
                HomeState.Loading
            }
            is HomeResult.ExchangeRateSuccess -> {
                HomeState.ExchangeRateSuccess(result.exchangeRates)
            }
            is HomeResult.ExchangeRateFailure -> {
                HomeState.ExchangeRateFailure(result.failure)
            }
        }.exhaustive
    }

    override fun onCleared() {
        Timber.i("HomeViewModel: onCleared()")
        super.onCleared()
    }

}