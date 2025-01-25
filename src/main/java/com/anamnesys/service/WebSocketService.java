package com.anamnesys.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class WebSocketService {

    // Armazena as sessões ativas
    private Set<WebSocketSession> sessions = new HashSet<>();

    // Método para adicionar uma nova sessão ao conjunto de sessões
    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    // Método para remover uma sessão quando ela é fechada
    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    // Método para enviar uma notificação para um usuário específico (baseado em userId)
    public void sendNotification(String message, String userId) {
        System.out.println("Sessões ativas: " + sessions.size());
        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    // Envia a mensagem para a sessão específica
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }


    // Método para enviar uma notificação para todas as sessões
    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
