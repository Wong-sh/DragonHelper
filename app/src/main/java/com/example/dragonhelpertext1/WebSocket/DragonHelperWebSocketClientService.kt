package com.example.dragonhelpertext1.WebSocket

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.recyclerview.widget.AsyncListUtil
import org.json.JSONObject

class DragonHelperWebSocketClientService : Service(){

    private var serviceRunning: Boolean = false
    var client: DragonHelperWebSocketClient? = null
    private  var event1: Int = 100
    private var event2: Int = 100
    private var createTime: String? = null
    private var lastCreateTime: String? = null
    private var errorMsgList= ArrayList<String>()
    private val myBinder: MyWebSocketClientBinder = MyWebSocketClientBinder()

    //Service必须实现的方法，用于返回Binder对象
    override fun onBind(p0: Intent?): IBinder {

        return myBinder
    }

    //用于Activity与Service的通信
    internal inner class MyWebSocketClientBinder : Binder(){

        fun getService() : MyWebSocketClientBinder{
            return this
        }
    }

    override fun onCreate() {
        Log.e("WebSocketClientService", "服务启动")
        //获得websocketclient
        getWebSocketClient()
        //开启心跳检测,HEART_BEAT_RATE秒后调用对象
        myHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE)
        //开启信息监听,MESSAGE_LISTEN秒后调用对象
        myHandler.postDelayed(messageListenRunnable, MESSAGE_LISTEN)
        super.onCreate()
    }

    override fun onDestroy() {
        serviceRunning = false
        closeConnect()
        super.onDestroy()
    }

    //------------------------------------------------------------------------------------------------------获取WebSocket------------------------------------------------------------------------------------------------------------------------

    //获取client
    private fun getWebSocketClient(){

        client = WebSocketInitUtils.getClient()
    }

    private fun sendMessage( msg: String ){
        if ( client != null ){
            Log.e("WebSocketClientService", "发送消息：${msg.toByteArray()}")
            client!!.send(msg.toByteArray())
        }
    }

    private fun closeConnect(){
        Thread{
            try{
                client?.close()
            }catch (e: InterruptedException){
                e.printStackTrace()
            }
        }.start()
    }

    //-----------------------------------------------------------------------------------------------------------心跳检测,以及信息监听----------------------------------------------------------------------------------------------------------------------

    private val HEART_BEAT_RATE = ( 30 * 1000 ).toLong()//每30秒检测一次
    private val MESSAGE_LISTEN = ( 0.5 * 1000 ).toLong()//每0.5秒都监听一次是否有信息传来
    private var myHandler = Handler()

    private var heartBeatRunnable: Runnable = object : Runnable{

        override fun run(){

            if ( client != null ){
                if ( client!!.isClosed ){
                    reconnect()
                }
            } else {
                getWebSocketClient()
            }
            val json = JSONObject()
            json.put("event1", 2)
            json.put("event2", 0)
            json.put("callCode", 0)
            val body = JSONObject()
            json.put("body", body)

            sendMessage(json.toString())
            myHandler.postDelayed(this,HEART_BEAT_RATE)

        }
    }

    private var messageListenRunnable: Runnable = object : Runnable{

        @SuppressLint("LongLogTag")
        override fun run(){

            if ( client != null ){
                if ( client!!.isClosed ){
                    reconnect()
                }
            } else {
                getWebSocketClient()
            }
            event1 = WebSocketInitUtils.getEvent1()!!
            event2 = WebSocketInitUtils.getEvent2()!!
            errorMsgList = WebSocketInitUtils.getErrorMsgList()!!
            createTime = WebSocketInitUtils.getCreatTime()

            //因为是循环监听，所以如果监听到的信息没改变，则不再发送
            if ( event1 == 5 && event2 == 0 ){
                if ( lastCreateTime != createTime ){
                    Log.e("Service传给MainActivity的信息","$errorMsgList")
                    val intent = Intent()
                    intent.action = "ALL_DATA"
                    intent.putStringArrayListExtra("errorMsgList",errorMsgList)
                    sendBroadcast(intent)
                    lastCreateTime = createTime
                }
            }
            myHandler.postDelayed(this,MESSAGE_LISTEN)
        }
    }

    //重连
    private fun reconnect(){

        //先关闭心跳检测和信息监听
        myHandler.removeCallbacks(heartBeatRunnable)
        myHandler.removeCallbacks(messageListenRunnable)
        Thread{
            try{
                Log.e("WebSocketClientService", "开始重连")
                client?.reconnectBlocking()
            } catch ( e: InterruptedException){
                e.printStackTrace()
            }
        }.start()
    }
}