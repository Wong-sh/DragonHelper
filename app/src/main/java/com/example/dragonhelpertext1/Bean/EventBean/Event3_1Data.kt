package com.example.dragonhelpertext1.Bean.EventBean

class Event3_1Data (private val id: Int, private val nickName: String, private val online: Int, private val phone: String, private val rent: Int){

    override fun toString(): String {
        return (" [id=$id, nickName=$nickName, online=$online, phone=$phone, rent=$rent]")
    }

    fun getID(): Int {
        return id
    }

    fun getNickTime(): String {
        return nickName
    }

    fun getOnline(): Int {
        return online
    }

    fun getPhone(): String {
        return phone
    }

    fun getRent(): Int {
        return rent
    }

}