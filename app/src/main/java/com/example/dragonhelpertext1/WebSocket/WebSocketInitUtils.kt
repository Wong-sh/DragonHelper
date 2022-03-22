package com.example.dragonhelpertext1.WebSocket

import android.util.Log
import com.alibaba.fastjson.JSON
import com.example.dragonhelpertext1.Bean.EventBean.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI
import java.nio.ByteBuffer

class WebSocketInitUtils {

    companion object{

        lateinit var client: DragonHelperWebSocketClient
        var event1: Int = 100
        var event2: Int = 100
        var errorCode: Int = 100
        var message: String? = null
        var data3: List<Event3_1Data>? = null
        var data5: List<Event5_0Data>? = null

        var id: Int = 100
        var nickName: String? = null
        var online: Int = 100
        var phone: String? = null
        var rent: Int = 100

        var createTime: String? = null
        var errorLevel: Int = 100
        var errormsg: String? = null

        var idList = ArrayList<String>()
        var nickNameList = ArrayList<String>()
        var onlineList = ArrayList<String>()
        var phoneList = ArrayList<String>()
        var rentList = ArrayList<String>()
        var errormsgList = ArrayList<String>()

        //对WebSocket初始化
        fun initWebSocket() {

            val uri = URI.create(getUri())
            client = object : DragonHelperWebSocketClient(uri) {
                override fun onMessage(bytes: ByteBuffer?) {
                    //重新onMessage,接收信息

                    //把bytes转成byteArray,然后把byteArray转成String,最后用fastjson获得原来数据json
                    //用gson获取返回信息里的各个信息
                    val byteArray = bytes?.array()
                    val string = String(byteArray!!)
                    val json: com.alibaba.fastjson.JSONObject? = JSON.parseObject(string)
                    //读取表层信息
                    val type = object : TypeToken<EventMessage<Event0_0>>(){}.type
                    event1 = Gson().fromJson<EventMessage<Event0_0>>(string,type).event1
                    event2 = Gson().fromJson<EventMessage<Event0_0>>(string,type).event2
                    errorCode = Gson().fromJson<EventMessage<Event0_0>>(string,type).errorCode
                    message = Gson().fromJson<EventMessage<Event0_0>>(string,type).message
                    //读取3_1中数组信息
                    val type3 = object : TypeToken<EventMessage<Event3_1>>(){}.type
                    if ( event1 == 3 && event2 == 1 ){
                        data3 = Gson().fromJson<EventMessage<Event3_1>>(string,type3).body.data
                        if ( data3 != null ){
                            //循环获取个人信息
                            val dataLenght = data3!!.size
                            for ( index in 0 until dataLenght){
                                val jsonObject: Event3_1Data = data3!![index]
                                id = jsonObject.getID()
                                idList.add(id.toString())
                                nickName = jsonObject.getNickTime()
                                nickNameList.add(nickName!!)
                                online = jsonObject.getOnline()
                                if ( online.toString() == "1" ){
                                    onlineList.add("在线")
                                } else if ( online.toString() == "0" ){
                                    onlineList.add("离线")
                                }
                                phone = jsonObject.getPhone()
                                phoneList.add(phone!!)
                                rent = jsonObject.getRent()
                                rentList.add(rent.toString())
                            }
                        }
                    }
                    //读取5_0中数组信息(目前只获取errormsg,createTime)
                    val type5 = object : TypeToken<EventMessage<Event5_0>>(){}.type
                    if ( event1 == 5 && event2 == 0 ){
                        data5 = Gson().fromJson<EventMessage<Event5_0>>(string,type5).body.data
                        if ( data5 != null ){
                            //循环获取errormsg
                            val dataLenght = data5!!.size
                            for ( index in 0 until dataLenght){
                                val jsonObject: Event5_0Data = data5!![index]
                                errormsg = jsonObject.getErrorMsg()
//                                errormsgList.clear()
                                errormsgList.add(errormsg!!)
                            }
                            //获取第一条的createTime
                            val jsonObjectOnce: Event5_0Data = data5!![0]
                            createTime = jsonObjectOnce.getCreateTime()
                        } else {
                            errormsg = null
                            errormsg?.let { errormsgList.add(it) }
                        }
                    }
                    Log.e("WebSocketInitUtils", "接收信息：$json")
                }
            }
            //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
            client.connectBlocking()
        }

        //获得目标地址
        private fun getUri(): String{
            return "ws://192.168.0.119:8800/ws"
        }

        @JvmName("getClient1")
        fun getClient(): DragonHelperWebSocketClient {
            return client
        }

        @JvmName("getEvent1real")
        fun getEvent1(): Int? {
            return event1
        }

        @JvmName("getEvent2real")
        fun getEvent2(): Int? {
            return event2
        }

        @JvmName("getErrorCodereal")
        fun getErrorCode(): Int? {
            return errorCode
        }

        @JvmName("getMessagereal")
        fun getMessage(): String? {
            return message
        }

        @JvmName("getIDListreal")
        fun getIDList(): ArrayList<String> {
            return idList
        }

        @JvmName("getNickNameListreal")
        fun getNickNameList(): ArrayList<String> {
            return nickNameList
        }

        @JvmName("getOnlineListreal")
        fun getOnlineList(): ArrayList<String> {
            return onlineList
        }

        @JvmName("getPhoneListreal")
        fun getPhoneList(): ArrayList<String> {
            return phoneList
        }

        @JvmName("getRentListreal")
        fun getRentList(): ArrayList<String> {
            return rentList
        }

        @JvmName("getErrorMsgListreal")
        fun getErrorMsgList(): ArrayList<String> {
            return errormsgList
        }

        @JvmName("getCrerateTimereal")
        fun getCreatTime(): String? {
            return createTime
        }
    }
}



