package com.spevec.bitfinex.client.data.websocket

import io.reactivex.Flowable

interface WebSocket {

    fun connect(): Flowable<Event>

    fun send(message: String): Boolean

    fun close(shutdownRequest: ShutdownRequest): Boolean

    fun cancel()

    sealed class Event {

        data class OnConnectionEstablished(val webSocket: okhttp3.WebSocket) : Event()

        data class OnMessageReceived(val message: String) : Event()

        data class OnConnectionClosing(val shutdownRequest: ShutdownRequest) : Event()

        data class OnConnectionClosed(val shutdownRequest: ShutdownRequest) : Event()

        data class OnConnectionFailed(val throwable: Throwable) : Event()

        object None: Event()
    }

    interface Factory {

        fun create(): WebSocket
    }
}
