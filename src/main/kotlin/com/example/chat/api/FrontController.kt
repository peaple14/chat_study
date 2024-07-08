package com.example.chat.api

import com.example.chat.data.Room
import com.example.chat.service.RoomService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
@CrossOrigin(origins = ["*"])
class FrontController() {

    @GetMapping("/")
    fun mainPage(): String {
        return "main"
    }


    @GetMapping("/chat/{id}")
    fun getChatPage(): String {
        println("/chats 들어옴")
        return "chat"
    }
}
