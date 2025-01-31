package com.anamnesys.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "hJk1PmS7n78XyP4q5wZjF4k6VOr0r4rP";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;


    public static String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .sign(ALGORITHM);
    }

    public static String generateResetPassWordToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);
    }

    public static DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .build();
        return verifier.verify(token);
    }

    public static boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }

    public static DecodedJWT verifyTokenByResetPassword(String token) {
        String extractTokenFromRequest = extractTokenFromRequest(token);
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .build();
        return verifier.verify(extractTokenFromRequest);
    }
    private static String extractTokenFromRequest(String authHeader) {
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }
}

