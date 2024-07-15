package com.example.chat.service

import com.example.chat.data.Room
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoomService {
    private val room: MutableMap<UUID, Room> = mutableMapOf()

    fun getAllRooms(): List<Room> {
        return room.values.toList()
    }

    fun createRoom(name: String): Room {
        val roomId = UUID.randomUUID()
        val room = Room(roomId, name)
        this.room[roomId] = room
        return room
    }

    fun getRoomByName(name: String): Room? {
        return room.values.find { it.name == name }
    }
}
