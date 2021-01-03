package com.ibrahim.home

import com.ibrahim.core.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class HomeRepository @Inject constructor(private val remoteDataSource: IHomeRemoteDataSource) :
    IHomeRepository {
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
                    false -> emit(
                        HomeResult.ExchangeRateFailure(
                            Failure.ServerError(
                                response.error?.code,
                                response.error?.type,
                                response.error?.info
                            )
                        )
                    )
                }
            } catch (exception: Throwable) {
                Timber.i(exception.localizedMessage)
                emit(HomeResult.ExchangeRateFailure(Failure.NetworkConnection(exception)))
            }

        }.flowOn(Dispatchers.IO)
    }
}