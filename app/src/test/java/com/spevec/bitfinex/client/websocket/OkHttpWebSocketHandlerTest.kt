package com.spevec.bitfinex.client.websocket

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.spevec.bitfinex.client.data.websocket.okhttp.OkHttpWebSocketHandlerImpl
import okhttp3.WebSocket
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo

import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class OkHttpWebSocketHandlerTest {

    private val webSocket = mock<WebSocket> {
        on { send(any<String>()) } doReturn true
        on { close(any(), any()) } doReturn true
    }

    private val webSocketHolder = OkHttpWebSocketHandlerImpl()

    @Test
    fun sendTextWebSocketIsReady() {
        webSocketHolder.initiate(webSocket)
        val text = "Test"
        val isSuccessful = webSocketHolder.send(text)

        assertThat(isSuccessful, `is`(equalTo(true)))

        then(webSocket).should().send(text)
    }

    @Test
    fun sendTextWebSocketIsNotReady() {
        val text = "Hello"
        val isSuccessful = webSocketHolder.send(text)

        assertThat(isSuccessful, `is`(equalTo(false)))
    }

    @Test
    fun closeWebSocketIsReady() {
        webSocketHolder.initiate(webSocket)
        val code = 5432
        val reason = "Test"
        val isSuccessful = webSocketHolder.close(code, reason)

        then(webSocket).should().close(code, reason)
        assertThat(isSuccessful, `is`(equalTo(true)))
    }

    @Test
    fun closeWebSocketIsNotReady() {
        val code = 5432
        val reason = "Test"
        val isSuccessful = webSocketHolder.close(code, reason)

        assertThat(isSuccessful, `is`(equalTo(false)))
    }
}