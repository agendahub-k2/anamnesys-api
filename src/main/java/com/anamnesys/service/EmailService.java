package com.anamnesys.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String from;

    public void enviarEmail(String para, String assunto, String mensagemHtml) {
        try {
            // Criar a mensagem de e-mail
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            // Configurar e-mail
            helper.setFrom(from);
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(mensagemHtml, true); // O `true` indica que é HTML

            // Enviar e-mail
            mailSender.send(mensagem);
            logger.info("E-mail enviado com sucesso!");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }
}

