package com.spevec.bitfinex.client.data.repository

import com.spevec.bitfinex.client.data.api.mapper.ResponseApiMapper
import com.spevec.bitfinex.client.data.api.model.ChannelType
import com.spevec.bitfinex.client.data.model.OrderBook
import com.spevec.bitfinex.client.data.model.Ticker
import com.spevec.bitfinex.client.data.websocket.WebSocketConnectionManager
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class BitfinexRepositoryImpl(
        private val webSocketConnectionManager: WebSocketConnectionManager,
        private val responseApiMapper: ResponseApiMapper
) : BitfinexRepository {

    companion object {
        private const val TIMER_IN_MILLISECONDS = 1500L
        private const val NUMBER_OF_ORDER_BOOK_ITEMS = 20
    }

    private var latestTicker: Ticker? = null

    override fun getTicker(): Flowable<Ticker> =
            webSocketConnectionManager.getWebSocketData()
                    .filter { it.channelType == ChannelType.TICKER }
                    .map { responseApiMapper.mapToTickerResponse(it.data) }
                    .doOnNext { latestTicker = it }
                    .startWith(getLatestTickerOrEmpty())

    override fun getOrderBook(): Flowable<List<OrderBook>> =
            webSocketConnectionManager.getWebSocketData()
                    .filter { it.channelType == ChannelType.BOOK }
                    .map { responseApiMapper.mapToOrderBookResponse(it.data) }
                    .flatMap { list -> Flowable.fromIterable(list) }
                    .buffer(NUMBER_OF_ORDER_BOOK_ITEMS)
                    .throttleFirst(TIMER_IN_MILLISECONDS, TimeUnit.MILLISECONDS)

    override fun terminate(): Completable = Completable.fromAction { webSocketConnectionManager.terminate() }

    private fun getLatestTickerOrEmpty(): Ticker = latestTicker ?: Ticker.EMPTY
}