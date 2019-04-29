package com.spevec.bitfinex.client.data.websocket.okhttp

import com.spevec.bitfinex.client.data.websocket.ShutdownRequest
import com.spevec.bitfinex.client.data.websocket.WebSocket
import io.reactivex.Flowable
import okhttp3.WebSocketListener

class OkHttpWebSocket(
    private val okHttpWebSocketHandler: OkHttpWebSocketHandler,
    private val okHttpWebSocketEventListener: OkHttpWebSocketEventListener,
    private val okHttpConnectionCreator: OkHttpConnectionCreator
) : WebSocket {

    private var isConnectionEstablished: Boolean = false

    override fun connect(): Flowable<WebSocket.Event> = okHttpWebSocketEventListener.getWebSocketEvents()
        .doOnSubscribe { if (!isConnectionEstablished) establishConnection(okHttpWebSocketEventListener) }
        .doOnNext(this::handleWebSocketEvent)

    @Synchronized
    override fun send(message: String): Boolean = okHttpWebSocketHandler.send(message)

    @Synchronized
    override fun close(shutdownRequest: ShutdownRequest): Boolean {
        val (code, reasonText) = shutdownRequest
        return okHttpWebSocketHandler.close(code, reasonText)
    }

    @Synchronized
    override fun cancel() = okHttpWebSocketHandler.cancel()

    private fun handleWebSocketEvent(event: WebSocket.Event) {
        when (event) {
            is WebSocket.Event.OnConnectionEstablished -> {
                this.isConnectionEstablished = true
                okHttpWebSocketHandler.initiate(event.webSocket)
            }
            is WebSocket.Event.OnConnectionClosing -> close(ShutdownRequest.GRACEFUL)
            is WebSocket.Event.OnConnectionClosed, is WebSocket.Event.OnConnectionFailed -> {
                this.isConnectionEstablished = false
                handleConnectionShutdown()
            }
        }
    }

    @Synchronized
    private fun handleConnectionShutdown() {
        okHttpWebSocketHandler.shutdown()
        okHttpWebSocketEventListener.complete()
    }

    private fun establishConnection(webSocketListener: WebSocketListener) = okHttpConnectionCreator.createConnection(webSocketListener)
}