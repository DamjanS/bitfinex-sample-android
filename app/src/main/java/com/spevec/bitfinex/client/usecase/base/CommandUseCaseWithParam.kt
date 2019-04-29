package com.spevec.bitfinex.client.usecase.base

import io.reactivex.Completable

interface CommandUseCaseWithParam<Param> {

    operator fun invoke(param: Param): Completable
}