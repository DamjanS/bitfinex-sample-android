package com.spevec.bitfinex.client.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.spevec.bitfinex.client.ui.model.MainResponse
import com.spevec.bitfinex.client.usecase.QueryOrderBookUseCase
import com.spevec.bitfinex.client.usecase.QueryTickerUseCase
import com.spevec.bitfinex.client.usecase.ShutdownConnectionUseCase
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables

class MainViewModel(
        private val queryTickerUseCase: QueryTickerUseCase,
        private val queryOrderBookUseCase: QueryOrderBookUseCase,
        private val backgroundScheduler: Scheduler,
        private val mainScheduler: Scheduler
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val response = MutableLiveData<MainResponse>()

    fun mainResponse(): MutableLiveData<MainResponse> = response

    fun getMainResponse() {
        disposables.add(
                Flowables.combineLatest(
                        queryTickerUseCase(),
                        queryOrderBookUseCase()
                ) { ticker, orderBooks -> MainResponse(ticker, orderBooks) }
                        .subscribeOn(backgroundScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                this::dispatchUpdates,
                                this::logError
                        )
        )
    }

    private fun dispatchUpdates(mainResponse: MainResponse) {
        response.value = mainResponse
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun logError(throwable: Throwable) {
        Log.e("Error", "Something went wrong ${throwable.printStackTrace()}")
    }
}