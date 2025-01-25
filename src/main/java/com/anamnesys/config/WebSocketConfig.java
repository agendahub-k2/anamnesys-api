package com.anamnesys.config;

import com.anamnesys.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketService webSocketService;

    // Injeção do serviço WebSocket
    @Autowired
    public WebSocketConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Registra o WebSocketHandler
        registry.addHandler(new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                // Adiciona a sessão ao serviço após a conexão ser estabelecida
                webSocketService.addSession(session);
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                // Pode adicionar lógica para lidar com mensagens recebidas
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                // Lógica de erro, se necessário
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // Remove a sessão quando a conexão for fechada
                webSocketService.removeSession(session);
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        }, "/ws").setAllowedOrigins("*");
    }
}
