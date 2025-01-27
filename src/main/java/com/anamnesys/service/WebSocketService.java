package com.anamnesys.service;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketService {

    // Mapeia userId para suas sessões WebSocket
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // Adiciona uma nova sessão para um usuário específico
    public void addSession(String userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    // Remove a sessão quando ela é fechada
    public void removeSession(String userId) {
        userSessions.remove(userId);
    }

    // Envia uma notificação para um usuário específico
    public void sendNotification(String message, String userId, String topic) {
        ObjectMapper objectMapper = new ObjectMapper();

        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(
                        new Notification(topic, message)
                );
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            }
        } else {
            System.out.println("Sessão para usuário " + userId + " não encontrada ou está fechada.");
        }
    }

    // Envia uma notificação para todos os usuários conectados
    public void broadcast(String message, String topic) {
        ObjectMapper objectMapper = new ObjectMapper();

        userSessions.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    String jsonMessage = objectMapper.writeValueAsString(
                            new Notification(topic, message)
                    );
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    System.err.println("Erro ao enviar mensagem: " + e.getMessage());
                }
            }
        });
    }
}

class Notification {
    private String type;
    private String content;

    public Notification(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
