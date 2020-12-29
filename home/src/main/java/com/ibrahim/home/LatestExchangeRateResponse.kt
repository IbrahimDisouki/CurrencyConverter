package com.ibrahim.home

import com.fasterxml.jackson.annotation.JsonProperty

data class LatestExchangeRateResponse(
    @JsonProperty("success")
    val success: Boolean? = null,
    @JsonProperty("timestamp")
    val timestamp: Long? = null,
    @JsonProperty("base")
    val base: String? = null,
    @JsonProperty("date")
    val date: String? = null,
    @JsonProperty("rates")
    val rates: Map<String, Double>? = null
)