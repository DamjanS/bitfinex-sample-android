package com.spevec.bitfinex.client.data.websocket.okhttp

import okhttp3.WebSocket
import okio.ByteString

class OkHttpWebSocketHandlerImpl : OkHttpWebSocketHandler {

    private var webSocket: WebSocket? = null

    override fun initiate(webSocket: WebSocket) {
        this.webSocket = webSocket
    }

    override fun shutdown() {
        webSocket = null
    }

    override fun send(text: String) = webSocket?.send(text) ?: false

    override fun send(bytes: ByteString) = webSocket?.send(bytes) ?: false

    override fun close(code: Int, reason: String?) = webSocket?.close(code, reason) ?: false

    override fun cancel() = webSocket?.cancel() ?: Unit
}