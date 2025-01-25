package com.anamnesys.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketService {
    private final SimpMessagingTemplate template;

    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sentNotification(String message, Long userId) {
        template.convertAndSend("/users/" + userId + "/notifications", message);
    }
}
