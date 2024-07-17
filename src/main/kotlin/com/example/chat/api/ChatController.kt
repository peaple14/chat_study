package com.example.chat.api

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ChatController {

    @GetMapping("/chats/{roomName}")
    fun chatRoom(@PathVariable roomName: String, model: Model): String {
        if (roomName.isBlank() || roomName.length !in 3..20 || !roomName.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            throw IllegalArgumentException("유효하지 않은 방 이름입니다: $roomName")
        }

        model.addAttribute("roomName", roomName)
        return "chat"
    }
}
