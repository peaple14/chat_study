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
) : TextWebSocketHandler() {

    @Scheduled(cron = "0 0 6 * * ?")
    fun clearSystem() {
        sessionList.forEach { it.close() }
        sessionList.clear()
        messageList.clear()
    }

    override fun handleTextMessage(session: WebSocketSession, textMessage: TextMessage) {
        sessionList.forEach { webSocketSession ->
            if (webSocketSession.isOpen) {
                webSocketSession.sendMessage(TextMessage(textMessage.payload))
                println("Received message: ${textMessage.payload}")
                val (author, message, time) = extractMessageInfo(textMessage.payload) ?: return
                messageList.add(Message.write(author, message, time))
            }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessionList.add(session)
        println("Connection established: ${session.id}")
        messageList.forEach { message ->
            session.sendMessage(TextMessage(message.toJson()))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionList.remove(session)
        println("Connection closed: ${session.id}")
    }

    fun extractMessageInfo(payload: String): Triple<String, String, String>? {
        val regex = """\{"author":"(.*?)","message":"(.*?)","time":"(.*?)"}""".toRegex()
        val matchResult = regex.find(payload)
        return matchResult?.let {
            val (author, message, time) = it.destructured
            Triple(author, message, time)
        }
    }
}
