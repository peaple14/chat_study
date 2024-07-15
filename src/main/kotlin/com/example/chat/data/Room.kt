package com.example.chat.data

import java.util.*

data class Room(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val users: List<String> = listOf()
)