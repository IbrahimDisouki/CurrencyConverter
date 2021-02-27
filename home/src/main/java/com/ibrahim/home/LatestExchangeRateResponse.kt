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
    val rates: Map<String, Double>? = null,
    @JsonProperty("error")
    val error: Error? = null
)

data class Error(
    @JsonProperty("code")
    val code: Int? = null,
    @JsonProperty("type")
    val type: String? = null,
    @JsonProperty("info")
    val info: String? = null
)