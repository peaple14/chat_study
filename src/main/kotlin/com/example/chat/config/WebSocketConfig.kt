package com.example.chat.config

import com.example.chat.handler.SocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(val socketHandler: SocketHandler) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(socketHandler, "/ws/chat/{roomId}").setAllowedOrigins("*")
    }
}