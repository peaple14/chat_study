package com.example.chat.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping

@Controller
@CrossOrigin(origins = ["*"])
class FrontController {

    @GetMapping("/chats")
    fun getChatPage(): String {
        println("/chats 들어옴")
        return "chat"
    }
}
