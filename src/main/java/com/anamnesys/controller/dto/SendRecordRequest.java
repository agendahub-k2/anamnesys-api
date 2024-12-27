package com.anamnesys.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SendRecordRequest {

    @NotNull(message = "Ficha não pode ser null.")
    @Min(value = 1, message = "O recordId deve ser maior que zero.")
    private Long recordId;

    @Future(message = "A data de expiração deve ser no futuro.")
    private LocalDateTime dateExpiration;

    @Size(max = 255, message = "A mensagem deve ter no máximo 255 caracteres.")
    private String message;

    private Boolean isSendWhatsapp;

    private Boolean isSendMail;

    @NotNull(message = "O clientId não pode ser nulo.")
    @Min(value = 1, message = "O clientId deve ser maior que zero.")
    private Long clientId;

}

