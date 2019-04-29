package com.spevec.bitfinex.client.data.api.model

import org.json.JSONArray

data class DataResponse(
        val channelType: ChannelType,
        val data: JSONArray
)