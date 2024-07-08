package com.example.chat.handler

import com.example.chat.data.Message
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SocketHandler(
    private val sessionList: ArrayList<WebSocketSession> = ArrayList(),
    private val messageList: ArrayList<Message> = ArrayList()
): TextWebSocketHandler() {

    // WebSocket 연결이 닫힐 때, 모든 세션을 닫고 메시지 리스트를 비우는 메서드입니다.
    @Scheduled(cron = "0 0 6 * * ?")
    fun clearSystem() {
        sessionList.forEach { it.close() } // 모든 세션을 닫습니다.
        sessionList.clear() // 세션 리스트를 비웁니다.
        messageList.clear() // 메시지 리스트를 비웁니다.
    }

    // 클라이언트로부터 텍스트 메시지를 받았을 때 호출되는 메서드입니다.
    override fun handleTextMessage(session: WebSocketSession, textMessage: TextMessage) {
        sessionList.forEach { webSocketSession ->
            if (webSocketSession.isOpen) {
                webSocketSession.sendMessage(TextMessage(textMessage.payload)) // 받은 메시지를 모든 연결된 세션에 전송합니다.
                println("한말:" + textMessage.payload)
                // 받은 메시지에서 작성자, 메시지 내용, 시간 정보를 추출합니다.
                val (author, message, time) = extractMessageInfo(textMessage.payload) ?: return
                // 메시지 객체를 생성하여 메시지 리스트에 추가합니다.
                messageList.add(Message.write(author, message, time))
            }
        }
    }

    // WebSocket 연결이 성립된 후 호출되는 메서드입니다.
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionList.add(session) // 연결된 세션을 세션 리스트에 추가합니다.

        // 기존에 저장된 모든 메시지를 새로 연결된 클라이언트에게 전송합니다.
        messageList.forEach { message ->
            session.sendMessage(TextMessage(message.toJson()))
        }
    }

    // WebSocket 연결이 닫힐 때 호출되는 메서드입니다.
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionList.remove(session) // 연결이 닫힌 세션을 세션 리스트에서 제거합니다.
    }

    // 주어진 JSON 문자열에서 작성자, 메시지 내용, 시간 정보를 추출하는 메서드입니다.
    fun extractMessageInfo(payload: String): Triple<String, String, String>? {
        val regex = """\{"author":"(.*?)","message":"(.*?)","time":"(.*?)"}""".toRegex()
        val matchResult = regex.find(payload)
        return matchResult?.let {
            val (author, message, time) = it.destructured
            Triple(author, message, time)
        }
    }

}
