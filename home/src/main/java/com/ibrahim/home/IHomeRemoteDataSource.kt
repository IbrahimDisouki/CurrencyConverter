package com.ibrahim.home

import retrofit2.http.GET

interface IHomeRemoteDataSource {
    @GET("latest")
    suspend fun getLatestExchangeRate(): LatestExchangeRateResponse
}