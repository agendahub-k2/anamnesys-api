package com.anamnesys.util;

public class Constants {
    public static final String USER_NOT_FOUND = "Usuário não encontrado.";
    public static final String PATIENT_NOT_FOUND = "Paciente não encontrado.";
    public static final String RECORD_NOT_FOUND = "Ficha não encontrada.";
    public static final String TEMPLATE_NOT_FOUND = "Template não encontrado.";
    public static final String ANSWER_NOT_FOUND = "Respostas não encontrada.";
    public static final String ANSWER_RECEIVED = "Ficha recebida.";
    public static final String PASSWORD_EMAIL_INVALID = "Email ou senha inválida.";
    public static final String UNAUTHORIZED = "Acesso negado: usuário não autorizado a acessar este recurso.";
    public static final String MESSAGE_RESET = "FlowForms - Redefinir Senha.";
    public static final String MESSAGE_SEND_SUBJECT= "FlowForms - Novo formulário.";
    public static final String MESSAGE_VERIFICATION_SUBJECT = "FlowForms - Código de Verificação";
    public static final String EMAIL_TEMPLATE =
            "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                    "        .email-container { max-width: 600px; margin: 20px auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                    "        .email-header { background-color: #4f46e5; color: #ffffff; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; }" +
                    "        .email-body { padding: 20px; font-size: 16px; color: #333333; line-height: 1.5; }" +
                    "        .email-body p { margin: 0 0 15px; }" +
                    "        .email-footer { background-color: #f4f4f9; padding: 15px; text-align: center; font-size: 14px; color: #999999; }" +
                    "        .email-button { " +
                    "            display: inline-block; " +
                    "            padding: 10px 20px; " +
                    "            background-color: #4f46e5; " +
                    "            color: #ffffff !important; " +  // Força a cor do texto
                    "            text-decoration: none; " +
                    "            border-radius: 4px; " +
                    "            font-weight: bold; " +
                    "            margin-top: 20px; " +
                    "            text-align: center;" +
                    "        }" +
                    "        .email-button:hover { background-color: #4338ca; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='email-container'>" +
                    "        <div class='email-header'>FlowForms</div>" +
                    "        <div class='email-body'>" +
                    "            <p>Olá, {name},</p>" +
                    "            <p>Esperamos que esteja bem!</p>" +
                    "            <p>Você recebeu um novo formulário para preencher. Clique no botão abaixo para acessá-lo:</p>" +
                    "            <a href='{formUrl}' class='email-button' style='color: #ffffff !important;'>Acessar Formulário</a>" + // Aplicando cor direto na tag
                    "            <p>Se você não solicitou este e-mail, pode ignorá-lo.</p>" +
                    "        </div>" +
                    "        <div class='email-footer'>" +
                    "            © 2025 FlowForms. Todos os direitos reservados." +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";


    public static final String EMAIL_TEMPLATE_RESET =
            "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                    "        .email-container { max-width: 600px; margin: 20px auto; background: #ffffff; border-radius: 8px; " +
                    "                          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                    "        .email-header { background-color: #4f46e5; color: #ffffff; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; }" +
                    "        .email-body { padding: 20px; font-size: 16px; color: #333333; line-height: 1.5; }" +
                    "        .email-body p { margin: 0 0 15px; }" +
                    "        .email-footer { background-color: #f4f4f9; padding: 15px; text-align: center; font-size: 14px; color: #999999; }" +
                    "        .email-button { display: inline-block; padding: 10px 20px; background-color: #4f46e5; color: #ffffff; " +
                    "                        text-decoration: none; border-radius: 4px; font-weight: bold; margin-top: 20px; }" +
                    "        .email-button:hover { background-color: #4338ca; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class=\"email-container\">" +
                    "        <div class=\"email-header\">FlowForms</div>" +
                    "        <div class=\"email-body\">" +
                    "            <p>Olá, {0},</p>" +
                    "            <p>Recebemos uma solicitação para redefinir a senha da sua conta no FlowForms.</p>" +
                    "            <p>Se foi você quem fez essa solicitação, clique no botão abaixo para criar uma nova senha:</p>" +
                    "            <a href=\"{1}\" class=\"email-button\">Redefinir Senha</a>" +
                    "            <p>Se você não solicitou a redefinição de senha, ignore este e-mail. Sua conta permanecerá segura.</p>" +
                    "        </div>" +
                    "        <div class=\"email-footer\">" +
                    "            © 2025 FlowForms. Todos os direitos reservados." +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

    public static final String EMAIL_TEMPLATE_VERIFICATION =
            "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                    "        .email-container { max-width: 600px; margin: 20px auto; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); overflow: hidden; }" +
                    "        .email-header { background-color: #4f46e5; color: #ffffff; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; }" +
                    "        .email-body { padding: 20px; font-size: 16px; color: #333333; line-height: 1.5; text-align: center; }" +
                    "        .email-body p { margin: 0 0 15px; }" +
                    "        .verification-code { font-size: 20px; font-weight: bold; background: #4f46e5; color: #ffffff; padding: 10px; display: inline-block; border-radius: 5px; margin: 15px 0; }" +
                    "        .email-footer { background-color: #f4f4f9; padding: 15px; text-align: center; font-size: 14px; color: #999999; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='email-container'>" +
                    "        <div class='email-header'>FlowForms</div>" +
                    "        <div class='email-body'>" +
                    "            <p>Seu código de verificação é:</p>" +
                    "            <div class='verification-code'>{verificationCode}</div>" +
                    "            <p>Insira este código para concluir seu processo de verificação.</p>" +
                    "            <p>Se você não solicitou este código, ignore este e-mail.</p>" +
                    "        </div>" +
                    "        <div class='email-footer'>" +
                    "            © 2025 FlowForms. Todos os direitos reservados." +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";




}
