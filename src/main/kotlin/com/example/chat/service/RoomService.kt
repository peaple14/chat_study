package com.example.chat.service

import com.example.chat.data.Room
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoomService {
    private val rooms: MutableMap<UUID, Room> = mutableMapOf()

    fun getAllRooms(): List<Room> {
        return rooms.values.toList()
    }

    fun createRoom(name: String, subscribe: String): UUID {
        val roomId = UUID.randomUUID()
        rooms[roomId] = Room(roomId, name, subscribe)
        return roomId
    }

    fun getRoomById(roomId: UUID): Room? {
        return rooms[roomId]
    }
}
