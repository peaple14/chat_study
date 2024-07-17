package com.example.chat.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class UserNameService {

    private val logger = LoggerFactory.getLogger(UserNameService::class.java)
    private val userNames = ConcurrentHashMap.newKeySet<String>()

    fun isUserNameDuplicated(userName: String): Boolean {
        if (userName.isBlank() || userName.length !in 3..20 || !userName.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            logger.error("유효하지 않은 사용자 이름: $userName")
            throw IllegalArgumentException("사용자 이름은 3자 이상 20자 이하의 영문자, 숫자, 밑줄(_)만 사용할 수 있습니다.")
        }
        val isDuplicated = !userNames.add(userName)
        if (isDuplicated) {
            logger.info("중복된 사용자 이름: $userName")
        } else {
            logger.info("사용자 이름 등록: $userName")
        }
        return isDuplicated
    }

    fun removeUserName(userName: String) {
        userNames.remove(userName)
        logger.info("사용자 이름 제거: $userName")
    }

}
