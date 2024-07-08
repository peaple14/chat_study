package com.example.chat.data

class Message(
    var author: String? = null,
    var message: String? = null,
    var time: String? = null
) {

    // Message 클래스는 채팅 메시지를 표현하는 데이터 클래스입니다.
    // author: 메시지 작성자의 이름
    // message: 전송된 메시지 내용
    // time: 메시지가 전송된 시간

    companion object {
        // 채팅 메시지를 생성하는 정적 메서드입니다.
        fun write(author: String, message: String, time: String): Message =
            Message(author, message, time)
    }

    // 객체를 JSON 형식의 문자열로 변환하는 메서드입니다.
    fun toJson(): String =
        "{\"author\":\"$author\",\"message\":\"$message\",\"time\":\"$time\"}"

}
