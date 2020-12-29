package com.ibrahim.home

import kotlinx.coroutines.flow.Flow

interface IHomeRepository {
    fun getLatestExchangeRate(): Flow<HomeResult>
}