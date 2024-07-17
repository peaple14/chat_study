package com.example.chat.data

import java.util.UUID

data class Room(
    val id: UUID = UUID.randomUUID(),
    val name: String
) {
    init {
        require(name.isNotBlank()) { "방 이름은 공백일 수 없습니다." }
        require(name.length in 3..20) { "방 이름은 3자 이상 20자 이하여야 합니다." }
        require(name.matches(Regex("^[a-zA-Z0-9_]+$"))) { "방 이름은 영문자, 숫자, 밑줄(_)만 사용할 수 있습니다." }
    }
}
