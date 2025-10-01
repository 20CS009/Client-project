package com.example.cpms;

import java.util.Base64;
import java.security.SecureRandom;

public class JwtSecretGenerator {
    public static void main(String[] args) {
        byte[] key = new byte[32]; // 256 bits = 32 bytes
        new SecureRandom().nextBytes(key);
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("jwt.secret=" + base64Key);
    }
}