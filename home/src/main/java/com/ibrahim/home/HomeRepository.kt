package com.ibrahim.home

import com.ibrahim.core.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepository @Inject constructor(private val remoteDataSource: IHomeRemoteDataSource) :
    IHomeRepository {
    @ExperimentalCoroutinesApi
    override fun getLatestExchangeRate(): Flow<HomeResult> {
        return flow {
            emit(HomeResult.Loading)
            try {
                val response = remoteDataSource.getLatestExchangeRate()
                when (response.success) {
                    true -> {
                        val exchangeRates: MutableList<ExchangeRate> = mutableListOf()
                        response.rates?.forEach {
                            exchangeRates.add(ExchangeRate(it.key, it.value))
                        }
                        emit(
                            HomeResult.ExchangeRateSuccess(
                                ExchangeRates(
                                    response.base!!,
                                    exchangeRates
                                )
                            )
                        )
                    }
                    false -> emit(HomeResult.ExchangeRateFailure(Failure.ServerError))
                }
            } catch (exception: Throwable) {
                emit(HomeResult.ExchangeRateFailure(Failure.NetworkConnection))
            }

        }.flowOn(Dispatchers.IO)
    }
}