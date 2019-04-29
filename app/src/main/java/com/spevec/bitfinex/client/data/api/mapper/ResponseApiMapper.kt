package com.spevec.bitfinex.client.data.api.mapper

import com.spevec.bitfinex.client.data.api.model.SubscriptionResponse
import com.spevec.bitfinex.client.data.model.OrderBook
import com.spevec.bitfinex.client.data.model.Ticker
import com.squareup.moshi.Moshi
import org.json.JSONArray

class ResponseApiMapper(private val moshi: Moshi) {

    companion object {
        private const val TICKER_CHANGE_POSITION = 4
        private const val TICKER_LAST_PRICE_POSITION = 6
        private const val TICKER_VOLUME_POSITION = 7
        private const val TICKER_HIGH_POSITION = 8
        private const val TICKER_LOW_POSITION = 9

        private const val ORDER_BOOK_PRICE_POSITION = 0
        private const val ORDER_BOOK_COUNT_POSITION = 1
        private const val ORDER_BOOK_AMOUNT_POSITION = 2
    }

    fun mapToSubscriptionResponse(message: String): SubscriptionResponse? = moshi.adapter(SubscriptionResponse::class.java).fromJson(message)

    fun mapToTickerResponse(tickerArray: JSONArray): Ticker {
        return tickerArray.run {
            val dailyChange = getDouble(TICKER_CHANGE_POSITION)
            val lastPrice = getDouble(TICKER_LAST_PRICE_POSITION)
            val volume = getDouble(TICKER_VOLUME_POSITION)
            val high = getDouble(TICKER_HIGH_POSITION)
            val low = getDouble(TICKER_LOW_POSITION)

            Ticker(lastPrice, dailyChange, volume, low, high)
        }
    }

    fun mapToOrderBookResponse(orderBookArray: JSONArray): List<OrderBook> {
        // if first is array -> snapshot has multiple items
        return if (orderBookArray[0] is JSONArray) {
            val orderBookResponses = ArrayList<OrderBook>()
            for (i in 0 until orderBookArray.length()) {
                orderBookResponses.add(mapToOrderBook(orderBookArray.getJSONArray(i)))
            }
            orderBookResponses
        } else {
            listOf(mapToOrderBook(orderBookArray))
        }
    }

    private fun mapToOrderBook(orderBookArray: JSONArray): OrderBook {
        return orderBookArray.run {
            val price = getDouble(ORDER_BOOK_PRICE_POSITION)
            val count = getInt(ORDER_BOOK_COUNT_POSITION)
            val amount = getDouble(ORDER_BOOK_AMOUNT_POSITION)
            OrderBook(price, count, amount)
        }
    }
 }