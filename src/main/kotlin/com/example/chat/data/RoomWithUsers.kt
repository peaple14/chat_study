package com.example.chat.data

data class RoomWithUsers(
    val room: Room,
    val users: List<String>
) {
    init {
        require(users.all { it.isNotBlank() }) { "사용자 이름은 공백일 수 없습니다." }
        require(users.all { it.length in 3..20 }) { "사용자 이름은 3자 이상 20자 이하여야 합니다." }
        require(users.all { it.matches(Regex("^[a-zA-Z0-9_]+$")) }) { "사용자 이름은 영문자, 숫자, 밑줄(_)만 사용할 수 있습니다." }
    }
}
