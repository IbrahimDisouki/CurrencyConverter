package com.ibrahim.core

import kotlinx.coroutines.flow.Flow

interface UseCase<out Type, in Params> {

    fun execute(params: Params): Flow<Type>

}

class None
