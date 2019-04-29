package com.spevec.bitfinex.client.usecase.base

import io.reactivex.Completable

interface CommandUseCase {

    operator fun invoke(): Completable
}