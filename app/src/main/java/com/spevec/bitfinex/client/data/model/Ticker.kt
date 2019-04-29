package com.spevec.bitfinex.client.data.model

data class Ticker(
        val lastPrice: Double,
        val dailyChange: Double,
        val volume: Double,
        val low: Double,
        val high: Double
) {
    companion object {
        val EMPTY = Ticker(0.0, 0.0, 0.0, 0.0, 0.0)
    }
}