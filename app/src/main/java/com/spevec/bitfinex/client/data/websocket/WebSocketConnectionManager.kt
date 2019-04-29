package com.spevec.bitfinex.client.data.websocket

import android.util.Log
import com.spevec.bitfinex.client.data.api.mapper.RequestApiMapper
import com.spevec.bitfinex.client.data.api.mapper.ResponseApiMapper
import com.spevec.bitfinex.client.data.api.model.ChannelType
import com.spevec.bitfinex.client.data.api.model.DataResponse
import com.spevec.bitfinex.client.data.websocket.okhttp.factory.OkHttpWebSocketFactory
import com.spevec.bitfinex.client.network.ConnectivityMonitor
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import org.json.JSONArray
import org.json.JSONTokener

/*
 Handle webSocket connection lifecycle and reconnects
*/
class WebSocketConnectionManager(
        private val okHttpWebSocketFactory: OkHttpWebSocketFactory,
        connectivityMonitor: ConnectivityMonitor,
        private val requestApiMapper: RequestApiMapper,
        private val responseApiMapper: ResponseApiMapper,
        private val backgroundScheduler: Scheduler
) {

    companion object {
        const val SUBSCRIBED_CHANNEL = "channel"
        const val MESSAGE_INFO_PREFIX = "{"
        const val MESSAGE_PAYLOAD_PREFIX = "["

        const val CHANNEL_ID_POSITION = 0
        const val CHANNEL_CONTENT_POSITION = 1
    }

    private var disposable: CompositeDisposable = CompositeDisposable()

    private var webSocket: WebSocket? = null

    private val webSocketSubscriptions: MutableMap<String, ChannelType> = HashMap()
    private val dataProcessor = BehaviorProcessor.create<DataResponse>().toSerialized()

    init {
        disposable.add(
                connectivityMonitor.isConnected()
                        .observeOn(backgroundScheduler)
                        .distinctUntilChanged()
                        .filter { it }
                        .switchMap { connectToWebSocket() }
                        .subscribe(this::handleWebSocketEvent, this::logError)
        )
    }

    fun getWebSocketData(): Flowable<DataResponse> = dataProcessor.onBackpressureBuffer()

    fun terminate() = webSocket?.close(ShutdownRequest.GRACEFUL)

    private fun handleWebSocketEvent(event: WebSocket.Event) {
        when (event) {
            is WebSocket.Event.OnConnectionEstablished -> subscribeToChannels()
            is WebSocket.Event.OnMessageReceived -> determineMessageTypeAndContent(event.message)
            is WebSocket.Event.OnConnectionClosed, is WebSocket.Event.OnConnectionFailed -> webSocket = null
        }
    }

    private fun subscribeToChannels() {
        webSocket?.send(requestApiMapper.mapToSubscriptionRequestJson("ticker"))
        webSocket?.send(requestApiMapper.mapToSubscriptionRequestJson("book"))
    }

    private fun logError(throwable: Throwable) {
        Log.e("Error", "Something went wrong $throwable")
    }

    private fun connectToWebSocket(): Flowable<WebSocket.Event> {
        if (webSocket == null) {
            webSocket = okHttpWebSocketFactory.create()
        }

        return webSocket!!.connect().observeOn(backgroundScheduler)
    }

    private fun determineMessageTypeAndContent(message: String) {
        if (message.contains(MESSAGE_INFO_PREFIX) && message.contains(SUBSCRIBED_CHANNEL)) {
            responseApiMapper.mapToSubscriptionResponse(message)?.run { webSocketSubscriptions.put(channelId, ChannelType.valueOf(channel.toUpperCase())) }

        } else if (message.startsWith(MESSAGE_PAYLOAD_PREFIX)) {
            //parsing would be much easier if response would be JSON :)
            val payload = JSONArray(JSONTokener(message))

            val channelId = payload[CHANNEL_ID_POSITION].toString()
            val channelType = webSocketSubscriptions[channelId]

            // should be heartbeat - no need to parse
            if (payload[CHANNEL_CONTENT_POSITION] is String) return

            channelType?.run { dataProcessor.onNext(DataResponse(channelType, payload.optJSONArray(CHANNEL_CONTENT_POSITION))) }
        }
    }
}