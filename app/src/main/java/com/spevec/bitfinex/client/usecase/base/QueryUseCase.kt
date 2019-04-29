package com.spevec.bitfinex.client.usecase.base

import io.reactivex.Flowable

interface QueryUseCase<Result> {

    operator fun invoke(): Flowable<Result>
}