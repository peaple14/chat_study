package com.example.chat

import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.TextMessage
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus

@Component
class ChatHandler : TextWebSocketHandler() {
    private val sessions = mutableListOf<WebSocketSession>()
    private val chatRooms = mutableMapOf<String, MutableList<WebSocketSession>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        println("New connection established: ${session.id}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        chatRooms.values.forEach { it.remove(session) }
        println("Connection closed: ${session.id}")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("Message received: ${message.payload}")
        val payload = message.payload.split(":", limit = 2)
        val chatRoomId = payload[0]
        val chatMessage = payload[1]

        chatRooms.computeIfAbsent(chatRoomId) { mutableListOf() }
        chatRooms[chatRoomId]?.forEach {
            if (it.isOpen) {
                println("Sending message to ${it.id}: $chatMessage")
                it.sendMessage(TextMessage("${session.id}: $chatMessage"))
            }
        }
    }
}
