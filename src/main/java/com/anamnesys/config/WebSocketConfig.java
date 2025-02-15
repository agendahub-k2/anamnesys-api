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

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                // Extrai o userId da query string
                String userId = extractUserIdFromSession(session);
                if (userId != null) {
                    webSocketService.addSession(userId, session);
                    System.out.println("Conexão estabelecida para userId: " + userId);
                } else {
                    System.out.println("Conexão rejeitada: userId não encontrado na query string.");
                    session.close(CloseStatus.BAD_DATA);
                }
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                // Pode adicionar lógica para lidar com mensagens recebidas, se necessário
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                // Lida com erros de transporte
                System.err.println("Erro no WebSocket para sessão: " + session.getId());
                exception.printStackTrace();
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                // Extrai o userId e remove a sessão
                String userId = extractUserIdFromSession(session);
                if (userId != null) {
                    webSocketService.removeSession(userId, session);
                    System.out.println("Conexão encerrada para userId: " + userId);
                }
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }

            private String extractUserIdFromSession(WebSocketSession session) {
                try {
                    // Obtém os parâmetros da query string
                    Map<String, String> queryParams = splitQuery(session.getUri().getQuery());
                    return queryParams.get("userId"); // Retorna o userId se presente
                } catch (Exception e) {
                    System.err.println("Erro ao extrair userId da sessão: " + e.getMessage());
                    return null;
                }
            }

            private Map<String, String> splitQuery(String query) {
                return query != null
                        ? Map.of(query.split("=")[0], query.split("=")[1]) // Simplesmente divide a string
                        : Map.of();
            }

        }, "/ws").setAllowedOrigins("*");
    }
}
