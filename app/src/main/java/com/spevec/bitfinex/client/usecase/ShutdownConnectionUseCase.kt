package com.spevec.bitfinex.client.usecase

import com.spevec.bitfinex.client.data.repository.BitfinexRepository
import com.spevec.bitfinex.client.usecase.base.CommandUseCase
import io.reactivex.Completable

class ShutdownConnectionUseCase(private val bitfinexRepository: BitfinexRepository) : CommandUseCase {

    override fun invoke(): Completable = bitfinexRepository.terminate()
}