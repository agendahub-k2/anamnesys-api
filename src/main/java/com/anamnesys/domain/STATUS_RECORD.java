package com.anamnesys.domain;

import lombok.Getter;

@Getter
public enum STATUS_RECORD {
    ENVIADO,
    ERRO,
    RECEBIDO,
    CANCELADO;
}