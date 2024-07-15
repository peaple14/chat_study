package com.example.chat.api

import com.example.chat.service.UserNameService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserNameController(
    private val userNameService: UserNameService
) {

    @GetMapping("/user/{userName}")
    @CrossOrigin(origins = ["*"])
    fun isUserNameDuplicated(@PathVariable userName: String) =
        userNameService.isUserNameDuplicated(userName)
}
