package com.spevec.bitfinex.client.data.websocket.okhttp

import com.spevec.bitfinex.client.data.websocket.ShutdownRequest
import com.spevec.bitfinex.client.data.websocket.WebSocket
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString

class OkHttpWebSocketEventListener : WebSocketListener() {

    private val processor = PublishProcessor.create<WebSocket.Event>().toSerialized()

    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) =
            processor.onNext(WebSocket.Event.OnConnectionEstablished(webSocket))

    override fun onMessage(webSocket: okhttp3.WebSocket, bytes: ByteString) =
            processor.onNext(WebSocket.Event.OnMessageReceived(String(bytes.toByteArray())))

    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) =
            processor.onNext(WebSocket.Event.OnMessageReceived(text))

    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) =
            processor.onNext(WebSocket.Event.OnConnectionClosing(ShutdownRequest(code, reason)))

    override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) =
            processor.onNext(WebSocket.Event.OnConnectionClosed(ShutdownRequest(code, reason)))

    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) =
            processor.onNext(WebSocket.Event.OnConnectionFailed(t))

    // add potencial backpressure handling
    fun getWebSocketEvents(): Flowable<WebSocket.Event> = processor.onBackpressureBuffer()

    fun complete() = processor.onComplete()
}