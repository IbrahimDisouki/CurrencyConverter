package com.ibrahim.home

import com.ibrahim.core.None
import com.ibrahim.core.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LatestExchangeRateUseCase @Inject constructor(private val repository: IHomeRepository) :
    UseCase<HomeResult, None> {
    override fun execute(params: None): Flow<HomeResult> {
        return repository.getLatestExchangeRate()
    }
}