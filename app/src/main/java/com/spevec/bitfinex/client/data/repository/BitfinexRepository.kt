package com.spevec.bitfinex.client.data.repository

import com.spevec.bitfinex.client.data.model.OrderBook
import com.spevec.bitfinex.client.data.model.Ticker
import io.reactivex.Completable
import io.reactivex.Flowable

interface BitfinexRepository {

    fun getTicker(): Flowable<Ticker>

    fun getOrderBook(): Flowable<List<OrderBook>>

    fun terminate(): Completable
}