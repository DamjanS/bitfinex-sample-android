package com.spevec.bitfinex.client.data.api.model

import com.squareup.moshi.JsonClass

const val PAIR = "BTCUSD"

@JsonClass(generateAdapter = true)
data class SubscriptionRequest(
        val event: String = "subscribe",
        val channel: String,
        val pair: String = PAIR
)