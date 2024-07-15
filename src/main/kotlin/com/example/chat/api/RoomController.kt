package com.example.chat.api

import com.example.chat.service.RoomService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@CrossOrigin(origins = ["*"])
@RequestMapping("/")
class RoomController(private val roomService: RoomService) {

    @GetMapping("/")
    fun mainPage(model: Model): String {
        val rooms = roomService.getAllRooms()
        model.addAttribute("rooms", rooms)
        return "index"
    }

    @GetMapping("/chats")
    fun getChatPage(): String {
        println("/chats 들어옴")
        return "chat"
    }
}
