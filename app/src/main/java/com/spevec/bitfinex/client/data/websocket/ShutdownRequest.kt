package com.spevec.bitfinex.client.data.websocket

data class ShutdownRequest(val code: Int, val reason: String) {

    companion object {
        private const val NORMAL_CLOSURE_STATUS_CODE = 1000
        private const val NORMAL_CLOSURE_REASON = "Normal shutdown"

        @JvmField
        val GRACEFUL = ShutdownRequest(NORMAL_CLOSURE_STATUS_CODE, NORMAL_CLOSURE_REASON)
    }
}