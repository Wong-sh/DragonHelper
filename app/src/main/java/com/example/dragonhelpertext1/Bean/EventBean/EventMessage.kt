package com.example.dragonhelpertext1.Bean.EventBean

class EventMessage<out M> (val body: M, val callCode: Int, val errorCode: Int, val event1: Int, val event2: Int, val message: String)