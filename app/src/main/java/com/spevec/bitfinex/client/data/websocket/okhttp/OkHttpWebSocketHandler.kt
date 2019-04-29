package com.spevec.bitfinex.client.data.websocket.okhttp

import okhttp3.WebSocket
import okio.ByteString

interface OkHttpWebSocketHandler {

    fun initiate(webSocket: WebSocket)

    fun send(text: String): Boolean

    fun send(bytes: ByteString): Boolean

    fun shutdown()

    fun close(code: Int, reason: String?): Boolean

    fun cancel()
}