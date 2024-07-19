package com.example.chat.api

import com.example.chat.service.UserNameService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/user")
class UserNameController(
    private val userNameService: UserNameService
) {

    private val logger = LoggerFactory.getLogger(UserNameController::class.java)

    @GetMapping("/{userName}")
    fun isUserNameDuplicated(@PathVariable userName: String): Boolean {
        // 유효성 검사
        if (userName.isBlank() || userName.length !in 3..20 || !userName.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            logger.error("유효하지 않은 사용자 이름: $userName")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 이름은 3자 이상 20자 이하의 영문자, 숫자, 밑줄(_)만 사용할 수 있습니다.")
        }

        return try {
            userNameService.isUserNameDuplicated(userName)
        } catch (e: Exception) {
            logger.error("사용자 이름 중복 확인 실패", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 이름 중복 확인에 실패했습니다.")
        }
    }
}
