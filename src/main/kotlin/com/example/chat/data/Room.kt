package com.example.chat.data

import java.util.*

data class Room(
    //주소값
    val id: UUID = UUID.randomUUID(),
    //방이름
    val name: String,
    //구독url
    val subscriptionUrl: String

)
