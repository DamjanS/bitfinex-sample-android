package com.spevec.bitfinex.client.ui.model

import com.spevec.bitfinex.client.data.model.OrderBook
import com.spevec.bitfinex.client.data.model.Ticker

data class MainResponse(
        val ticker: Ticker,
        val orderBooks: List<OrderBook>
)