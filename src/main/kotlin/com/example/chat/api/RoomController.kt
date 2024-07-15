package com.example.chat.api

import com.example.chat.data.RoomWithUsers
import com.example.chat.data.Room
import com.example.chat.service.RoomService
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/")
class RoomController(private val roomService: RoomService) {

    @GetMapping("/")
    fun mainPage(model: Model): List<Room> {
        val rooms = roomService.getAllRooms()
        model.addAttribute("rooms", rooms)
        println("방 목록: $rooms");
        return rooms
    }

    // 방을 생성하는 엔드포인트
    @PostMapping("/")
    @ResponseBody
    fun createRoom(@RequestParam name: String): Room {
        println("만든 이름:$name");
        return roomService.createRoom(name)
    }

    @GetMapping("/chats/{name}")
    fun getChatRoom(@PathVariable name: String): RoomWithUsers {
        val room = roomService.getRoomByName(name)
        val users = roomService.getUsersInRoom(name) // 방에 속한 사용자들 조회하는 메서드

        println("방 정보: $room, 사용자들: $users")
        return room?.let {
            RoomWithUsers(it, users)
        } ?: throw RuntimeException("Room not found with name: $name")
    }
}
