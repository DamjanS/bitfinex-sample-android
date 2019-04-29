package com.spevec.bitfinex.client.data.websocket.okhttp.factory

import com.spevec.bitfinex.client.data.websocket.WebSocket
import com.spevec.bitfinex.client.data.websocket.okhttp.OkHttpConnectionCreator
import com.spevec.bitfinex.client.data.websocket.okhttp.OkHttpWebSocket
import com.spevec.bitfinex.client.data.websocket.okhttp.OkHttpWebSocketEventListener
import com.spevec.bitfinex.client.data.websocket.okhttp.OkHttpWebSocketHandlerImpl

class OkHttpWebSocketFactory(private val okHttpConnectionCreator: OkHttpConnectionCreator) : WebSocket.Factory {

    override fun create(): WebSocket =
        OkHttpWebSocket(
            OkHttpWebSocketHandlerImpl(),
            OkHttpWebSocketEventListener(),
            okHttpConnectionCreator
        )
}