package com.spevec.bitfinex.client.usecase

import com.spevec.bitfinex.client.data.model.OrderBook
import com.spevec.bitfinex.client.data.repository.BitfinexRepository
import com.spevec.bitfinex.client.usecase.base.QueryUseCase
import io.reactivex.Flowable

class QueryOrderBookUseCase(private val bitfinexRepository: BitfinexRepository) : QueryUseCase<List<OrderBook>> {

    override fun invoke(): Flowable<List<OrderBook>> = bitfinexRepository.getOrderBook()
}