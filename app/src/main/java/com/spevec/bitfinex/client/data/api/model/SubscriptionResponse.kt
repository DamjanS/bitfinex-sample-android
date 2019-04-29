package com.spevec.bitfinex.client.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscriptionResponse(
        val event: String,
        val channel: String,
        @Json(name = "chanId") val channelId: String,
        val symbol: String,
        val pair: String
)