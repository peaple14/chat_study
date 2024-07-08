package com.example.chat.app

import org.springframework.stereotype.Service

@Service
class UserNameService(
    private val userNameList: MutableList<String> = mutableListOf()
) {

    // 사용자 이름이 이미 리스트에 존재하는지 여부를 확인하고,
    // 존재하지 않으면 리스트에 추가하는 메서드입니다.
    fun isUserNameDuplicated(userName: String): Boolean {
        return if (userNameList.contains(userName)) {
            true // 이미 존재하는 사용자 이름이므로 true 반환
        } else {
            userNameList.add(userName) // 사용자 이름을 리스트에 추가하고 false 반환
            false
        }
    }

}
