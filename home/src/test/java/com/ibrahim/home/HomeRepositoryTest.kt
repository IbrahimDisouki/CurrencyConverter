package com.ibrahim.home

import com.google.common.truth.Truth
import com.ibrahim.core.Failure
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class HomeRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val remoteDataSource = mock<IHomeRemoteDataSource>()
    private val repository = HomeRepository(testDispatcher, remoteDataSource)

    @Test
    fun getLatestExchangeRate_whenServerRespondWithSuccess_returnExchangeRates() = runBlockingTest {
        val expectedResults = listOf(
            HomeResult.Loading, HomeResult.ExchangeRateSuccess(
                ExchangeRates(
                    "EUR",
                    listOf(ExchangeRate("AED", 4.457433))
                )
            )
        )


        // Mock API Service
        remoteDataSource.stub {
            onBlocking {
                getLatestExchangeRate()
            } doReturn LatestExchangeRateResponse(
                true,
                1609670046,
                "EUR",
                "2021-01-03",
                mapOf(Pair("AED", 4.457433))
            )
        }

        val actualResults: MutableList<HomeResult> = mutableListOf()
        val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
        flow.collect { data ->
            actualResults.add(data)
        }

        Truth.assertThat(actualResults).isEqualTo(expectedResults)

    }

    @Test
    fun getLatestExchangeRate_whenServerRespondWithFailure_returnServerError() = runBlockingTest {
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
        remoteDataSource.stub {
            onBlocking { getLatestExchangeRate() } doReturn LatestExchangeRateResponse(
                success = false,
                error = Error(
                    101,
                    "invalid_access_key",
                    "You have not supplied a valid API Access Key. [Technical Support: support@apilayer.com]"
                )
            )
        }

        val actualResults: MutableList<HomeResult> = mutableListOf()
        val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
        flow.collect { data ->
            actualResults.add(data)
        }

        Truth.assertThat(actualResults).isEqualTo(expectedResults)

    }

    @Test(expected = UnknownHostException::class)
    fun getLatestExchangeRate_whenNoInternetConnection_returnUnknownHostException() =
        runBlockingTest {
            val expectedResults = listOf(
                HomeResult.Loading,
                HomeResult.ExchangeRateFailure(Failure.NetworkConnection(UnknownHostException()))
            )
            // Mock API Service
            remoteDataSource.stub {
                onBlocking { throw UnknownHostException() }
            }

            val actualResults: MutableList<HomeResult> = mutableListOf()
            val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
            flow.collect { data ->
                actualResults.add(data)
            }

            Truth.assertThat(actualResults).isEqualTo(expectedResults)

        }

    @Test(expected = SocketTimeoutException::class)
    fun getLatestExchangeRate_whenConnectionTimeout_returnSocketTimeoutException() =
        runBlockingTest {
            val expectedResults = listOf(
                HomeResult.Loading,
                HomeResult.ExchangeRateFailure(Failure.NetworkConnection(SocketTimeoutException()))
            )
            // Mock API Service
            remoteDataSource.stub {
                onBlocking { throw SocketTimeoutException() }
            }

            val actualResults: MutableList<HomeResult> = mutableListOf()
            val flow: Flow<HomeResult> = repository.getLatestExchangeRate()
            flow.collect { data ->
                actualResults.add(data)
            }

            Truth.assertThat(actualResults).isEqualTo(expectedResults)

        }

}