package com.ibrahim.home

import com.ibrahim.core.CoreIntent

sealed class HomeIntent : CoreIntent {
    object GetLatestExchangeRate : HomeIntent()
}