package com.example.dragonhelpertext1.Bean.EventBean

class Event5_0Data (private val createTime: String, private val errorCode: Int, private val errorLevel: Int, private val errormsg: String, private val isRead: Int, private val nickName: String, private val phone: String){

    override fun toString(): String {
        return (" [createTime=$createTime, errorCode=$errorCode, errorLevel=$errorLevel, errormsg=$errormsg, isRead=$isRead, nickName=$nickName, phone=$phone ]")
    }

     fun getErrorMsg(): String {
        return errormsg
    }

    fun getCreateTime(): String {
        return createTime
    }
}