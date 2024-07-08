package com.example.chat.config

import com.example.chat.handler.SocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val socketHandler: SocketHandler
): WebSocketConfigurer {

    // WebSocket 구성을 위한 설정 클래스입니다.
    // SocketHandler를 주입받아 사용합니다.
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        // "/chat" 경로에 대한 WebSocket 핸들러를 등록합니다.
        // 모든 출처에서의 접근을 허용합니다.
        registry.addHandler(socketHandler, "/chat")
            .setAllowedOrigins("*")
    }

}
