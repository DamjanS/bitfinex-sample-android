package com.spevec.bitfinex.client.usecase.base

import io.reactivex.Flowable

interface QueryUseCaseWithParam<Param, Result> {

    operator fun invoke(param: Param): Flowable<Result>
}