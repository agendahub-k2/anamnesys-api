package com.anamnesys.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeCacheService {

    @CachePut(value = "verificationCodes", key = "#email")
    public Long putCachedCode(String email, Long verificationCode) {
        return verificationCode;
    }

    @Cacheable(value = "verificationCodes", key = "#email")
    public Long getCachedCode(String email) {
        return null; // Se não existir no cache, retorna null
    }
}

