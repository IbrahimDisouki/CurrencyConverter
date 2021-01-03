package com.ibrahim.home

import com.google.common.truth.Truth
import com.ibrahim.core.Failure
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class HomeRepositoryTest {

    @Test
    fun getLatestExchangeRate_whenServerRespondWithSuccess_returnExchangeRates() = runBlocking {
        val expectedResults = listOf(
            HomeResult.Loading, HomeResult.ExchangeRateSuccess(
                ExchangeRates(
                    "EUR",
                    listOf(ExchangeRate("AED", 4.457433))
                )
            )
        )
        // Mock API Service
        val apiService = mock<IHomeRemoteDataSource>() {
            onBlocking { getLatestExchangeRate() } doReturn LatestExchangeRateResponse(
                true,
                1609670046,
                "EUR",
                "2021-01-03",
                mapOf(Pair("AED", 4.457433))
            )
        }

        val repository = HomeRepository(apiService)

        val actualResults: MutableList<HomeResult> = mutableListOf()
        val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
        flow.collect { data ->
            actualResults.add(data)
        }

        Truth.assertThat(actualResults).isEqualTo(expectedResults)

    }

    @Test
    fun getLatestExchangeRate_whenServerRespondWithFailure_returnServerError() = runBlocking {
        val expectedResults = listOf(
            HomeResult.Loading, HomeResult.ExchangeRateFailure(
                Failure.ServerError(
                    101,
                    "invalid_access_key",
                    "You have not supplied a valid API Access Key. [Technical Support: support@apilayer.com]"
                )
            )
        )
        // Mock API Service
        val apiService = mock<IHomeRemoteDataSource>() {
            onBlocking { getLatestExchangeRate() } doReturn LatestExchangeRateResponse(
                success = false,
                error = Error(
                    101,
                    "invalid_access_key",
                    "You have not supplied a valid API Access Key. [Technical Support: support@apilayer.com]"
                )
            )
        }

        val repository = HomeRepository(apiService)

        val actualResults: MutableList<HomeResult> = mutableListOf()
        val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
        flow.collect { data ->
            actualResults.add(data)
        }

        Truth.assertThat(actualResults).isEqualTo(expectedResults)

    }

    @Test(expected = UnknownHostException::class)
    fun getLatestExchangeRate_whenNoInternetConnection_returnUnknownHostException() = runBlocking {
        val expectedResults = listOf(
            HomeResult.Loading,
            HomeResult.ExchangeRateFailure(Failure.NetworkConnection(UnknownHostException()))
        )
        // Mock API Service
        val apiService = mock<IHomeRemoteDataSource>() {
            onBlocking { throw UnknownHostException() }
        }

        val repository = HomeRepository(apiService)

        val actualResults: MutableList<HomeResult> = mutableListOf()
        val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
        flow.collect { data ->
            actualResults.add(data)
        }

        Truth.assertThat(actualResults).isEqualTo(expectedResults)

    }

    @Test(expected = SocketTimeoutException::class)
    fun getLatestExchangeRate_whenConnectionTimeout_returnSocketTimeoutException() = runBlocking {
        val expectedResults = listOf(
            HomeResult.Loading,
            HomeResult.ExchangeRateFailure(Failure.NetworkConnection(SocketTimeoutException()))
        )
        // Mock API Service
        val apiService = mock<IHomeRemoteDataSource>() {
            onBlocking { throw SocketTimeoutException() }
        }

        val repository = HomeRepository(apiService)

        val actualResults: MutableList<HomeResult> = mutableListOf()
        val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
        flow.collect { data ->
            actualResults.add(data)
        }

        Truth.assertThat(actualResults).isEqualTo(expectedResults)

    }

}