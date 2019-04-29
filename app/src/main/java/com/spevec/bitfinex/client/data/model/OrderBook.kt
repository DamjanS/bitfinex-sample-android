package com.spevec.bitfinex.client.data.model

data class OrderBook(
        val price: Double,
        val count: Int,
        val amount: Double
)