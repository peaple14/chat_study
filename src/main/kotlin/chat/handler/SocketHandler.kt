package com.example.chat.handler

import com.example.chat.data.Message
import com.example.chat.service.RoomService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.CopyOnWriteArrayList

@Component
class SocketHandler(private val roomService: RoomService) : TextWebSocketHandler() {

    private val rooms: MutableMap<String, CopyOnWriteArrayList<WebSocketSession>> = mutableMapOf()
    private val logger = LoggerFactory.getLogger(SocketHandler::class.java)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val roomId = getRoomId(session)
        rooms.computeIfAbsent(roomId) { CopyOnWriteArrayList() }.add(session)

        val nickname = session.uri?.query?.split("=")?.lastOrNull() ?: "Guest" // 닉네임 추출

        logger.info("Connected: $nickname to room: $roomId")

        val joinMessage = Message("Server", "$nickname joined the chat.", dateFormat.format(Date()))
        roomService.addUserToRoom(roomId, nickname)
        broadcastMessage(roomId, joinMessage)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val roomId = getRoomId(session)
        rooms[roomId]?.remove(session)
        logger.info("Disconnected: ${session.id} from room: $roomId")
        val leaveMessage = Message("Server", "User ${session.id} left the chat.", dateFormat.format(Date()))
        roomService.removeUserFromRoom(getRoomId(session),session.id);
        broadcastMessage(roomId, leaveMessage)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val msgPayload = message.payload.trim()
            if (msgPayload.isBlank()) {
                logger.warn("Received blank message from: ${session.id}")
                return
            }

            val msg = Message.fromJson(msgPayload)
            val roomId = getRoomId(session)
            broadcastMessage(roomId, msg)
        } catch (e: Exception) {
            logger.error("Error handling message: ${message.payload}", e)
        }
    }

    private fun broadcastMessage(roomId: String, message: Message) {
        val jsonMessage = message.toJson()
        rooms[roomId]?.forEach {
            try {
                it.sendMessage(TextMessage(jsonMessage))
            } catch (e: Exception) {
                logger.error("Failed to send message to session: ${it.id}", e)
            }
        }
    }

    private fun getRoomId(session: WebSocketSession): String {
        return session.uri?.path?.split("/")?.last() ?: throw IllegalArgumentException("Room ID not found")
    }
}
