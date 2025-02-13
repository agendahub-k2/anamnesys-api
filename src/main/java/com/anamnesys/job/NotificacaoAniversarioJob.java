package com.anamnesys.job;

import com.anamnesys.repository.PatientRepository;
import com.anamnesys.repository.model.PatientModel;
import com.anamnesys.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NotificacaoAniversarioJob {

    private final PatientRepository patientRepository;
    private final WebSocketService webSocketService;
    private static final Logger logger = LoggerFactory.getLogger(NotificacaoAniversarioJob.class);

    @Autowired
    public NotificacaoAniversarioJob(PatientRepository patientRepository, WebSocketService webSocketService) {
        this.patientRepository = patientRepository;
        this.webSocketService = webSocketService;
    }

    @Scheduled(cron = "0 */3 * * * ?")
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
}
