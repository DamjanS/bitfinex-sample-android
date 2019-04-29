package com.spevec.bitfinex.client.network

import android.net.ConnectivityManager
import android.net.Network
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class ConnectivityMonitor : ConnectivityManager.NetworkCallback() {

    private val connectivityChangeProcessor: BehaviorProcessor<Boolean> = BehaviorProcessor.create()

    override fun onAvailable(network: Network?) = connectivityChangeProcessor.onNext(true)
    
    override fun onLost(network: Network?) = connectivityChangeProcessor.onNext(false)

    fun isConnected(): Flowable<Boolean> = connectivityChangeProcessor.onBackpressureLatest()
}