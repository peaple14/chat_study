package com.example.chat.api

import com.example.chat.app.UserNameService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserNameController(
    private val userNameService: UserNameService
) {

    // 사용자 이름이 중복되는지 확인하는 엔드포인트입니다.
    // @PathVariable을 통해 클라이언트에서 전달받은 userName을 받습니다.
    // CrossOrigin 어노테이션을 통해 모든 오리진(출처)에서의 요청을 허용합니다.
    @GetMapping("/user/{userName}")
    @CrossOrigin(origins = ["*"])
    fun isUserNameDuplicated(@PathVariable userName: String) =
        userNameService.isUserNameDuplicated(userName)

}
