package com.example.chat.api

import com.example.chat.data.Room
import com.example.chat.service.RoomService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@CrossOrigin(origins = ["*"])
class RoomController(private val roomService: RoomService) {

    @GetMapping("/api/rooms")
    fun mainPage(): List<Room> {
//        val rooms = roomService.getAllRooms()
        val rooms = listOf(
            Room(id = UUID.randomUUID(), name = "Room 1", subscriptionUrl = "/ws/" + UUID.randomUUID()),
            Room(id = UUID.randomUUID(), name = "Room 2", subscriptionUrl = "/ws/" + UUID.randomUUID()),
        )
        println(rooms)
        return rooms
    }
}