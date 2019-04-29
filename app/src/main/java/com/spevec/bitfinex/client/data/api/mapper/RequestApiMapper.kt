package com.spevec.bitfinex.client.data.api.mapper

import com.spevec.bitfinex.client.data.api.model.SubscriptionRequest
import com.squareup.moshi.Moshi

class RequestApiMapper(private val moshi: Moshi) {

    fun mapToSubscriptionRequestJson(subscriptionRequestChannel: String): String {
        return moshi.adapter(SubscriptionRequest::class.java).toJson(SubscriptionRequest(channel = subscriptionRequestChannel))
    }
}