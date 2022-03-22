package com.example.dragonhelpertext1.WebSocket

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer

open class DragonHelperWebSocketClient(serverUri: URI?) :
    WebSocketClient(serverUri, Draft_6455()) {
    override fun onOpen(handshakedata: ServerHandshake) {
        Log.e("MyWebSocketClient", "连接成功")
    }

    override fun onMessage(message: String?) {
        Log.e("MyWebSocketClient", "接收信息：$message")
    }

    override fun onMessage(bytes: ByteBuffer?) {
        Log.e("MyWebSocketClient", "接收信息：$bytes")
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        Log.e("MyWebSocketClient", "关闭连接：$code\t原因：$reason")
    }

    override fun onError(e: Exception) {
        Log.e("MyWebSocketClient", "连接错误：$e")
    }

}