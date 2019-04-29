package com.spevec.bitfinex.client.data.websocket.okhttp

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

class OkHttpConnectionCreator(private val url: String, private val okHttpClient: OkHttpClient) {

    fun createConnection(webSocketListener: WebSocketListener) {
        okHttpClient.newWebSocket(createConnectionRequest(url), webSocketListener)
    }

    private fun createConnectionRequest(url: String): Request = Request.Builder()
            .url(url)
            .build()
}