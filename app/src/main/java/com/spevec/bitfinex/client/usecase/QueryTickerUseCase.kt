package com.spevec.bitfinex.client.usecase

import com.spevec.bitfinex.client.data.model.Ticker
import com.spevec.bitfinex.client.data.repository.BitfinexRepository
import com.spevec.bitfinex.client.usecase.base.QueryUseCase
import io.reactivex.Flowable

class QueryTickerUseCase(private val bitfinexRepository: BitfinexRepository) : QueryUseCase<Ticker> {

    override fun invoke(): Flowable<Ticker> = bitfinexRepository.getTicker()
}