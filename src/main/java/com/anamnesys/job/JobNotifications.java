package com.anamnesys.job;

import com.anamnesys.repository.PatientRepository;
import com.anamnesys.repository.RecordSendRepository;
import com.anamnesys.repository.model.PatientModel;
import com.anamnesys.repository.model.RecordSendModel;
import com.anamnesys.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JobNotifications {

    private final PatientRepository patientRepository;
    private final RecordSendRepository recordSendRepository;
    private final WebSocketService webSocketService;
    private static final Logger logger = LoggerFactory.getLogger(JobNotifications.class);

    @Autowired
    public JobNotifications(PatientRepository patientRepository, RecordSendRepository recordSendRepository, WebSocketService webSocketService) {
        this.patientRepository = patientRepository;
        this.recordSendRepository = recordSendRepository;
        this.webSocketService = webSocketService;
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void verificarAniversariantes() {
        logger.info("Initiated NotificacaoAniversarioJob");

        try {
            LocalDate now = LocalDate.now();
            int month = now.getMonthValue();
            int day = now.getDayOfMonth();
            List<PatientModel> birthdays = patientRepository.findByBirth(month, day);
            logger.info("birthdays size: {}", birthdays.size());

            birthdays.forEach(it -> {
                String message = "🎉 Hoje é o aniversário de " + it.getName() + "!";
                logger.info("{} - userId: {}", message, it.getUserId());
                webSocketService.sendNotification(message, it.getUserId().toString(), "birth_topic");
            });

        } catch (Exception e) {
            logger.error("Error in NotificacaoAniversarioJob: ", e);
        }
    }

    @Scheduled(cron = "0 0 10,15 * * ?")
    public void enviarNotificacaoClientesComDataRetornoAmanha() {
        logger.info("Initiated enviarNotificaçãoClientesComDataRetorno");
        try {
            LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<RecordSendModel> byReturnDtBetween = recordSendRepository.findByReturnDtBetween(start, end);
            byReturnDtBetween.forEach(it -> {
                PatientModel client = patientRepository.findById(it.getClientId()).orElseThrow();
                String message = "Amanhã é o retorno de  " + client.getName() + "!";
                webSocketService.sendNotification(message, it.getUserId().toString(), "birth_topic");
            });

        } catch (RuntimeException e) {
            logger.error("Error in enviarNotificaçãoClientesComDataRetorno ", e);
        }
    }

    @Scheduled(cron = "0 */10 * * * ?")
    public void enviarNotificacaoClientesComDataRetornoHoje() {
        logger.info("Initiated enviarNotificaçãoClientesComDataRetornoHoje");
        try {
            LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<RecordSendModel> byReturnDtBetween = recordSendRepository.findByReturnDtBetween(start, end);
            byReturnDtBetween.forEach(it -> {
                PatientModel client = patientRepository.findById(it.getClientId()).orElseThrow();
                String message = "Hoje é o retorno de  " + client.getName() + "!";
                webSocketService.sendNotification(message, it.getUserId().toString(), "birth_topic");
            });

        } catch (RuntimeException e) {
            logger.error("Error in enviarNotificaçãoClientesComDataRetorno ", e);
        }
    }
}
