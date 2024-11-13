package com.anamnesys.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

    private static final String SECRET_KEY = "hJk1PmS7n78XyP4q5wZjF4k6VOr0r4rP";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);


    public static String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .sign(ALGORITHM);
    }

    public static DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .build();
        return verifier.verify(token);
    }
}

