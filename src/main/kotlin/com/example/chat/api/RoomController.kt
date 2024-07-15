package com.example.chat.api

import com.example.chat.data.Room
import com.example.chat.service.RoomService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/")
class RoomController(private val roomService: RoomService) {

    @GetMapping("/room_list")
    fun mainPage(model: Model): List<Room> {
        val rooms = roomService.getAllRooms()
        model.addAttribute("rooms", rooms)
        println("방 목록: $rooms");
        return rooms
    }

    // 방을 생성하는 엔드포인트
    @PostMapping("/room_make")
    fun createRoom(@RequestParam name: String): Room {
        println("만든 이름:$name");
        return roomService.createRoom(name)
    }

    @GetMapping("/chats/{name}")
    fun getChatRoom(@PathVariable name: String): Room? {
        val room = roomService.getRoomByName(name)

        println("방 정보:$room")

        return room
    }
}
