package com.example.chat.data

data class RoomWithUsers(
    val room: Room,
    val users: List<String>,
)