package com.example.CloudStorageDiploma.services;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HexFormat;

public class HelperService {
    private static final int LOG_ROUNDS = 14;

    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64ToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    public static String sha256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(data);
        return HexFormat.of().formatHex(hash);
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(LOG_ROUNDS));
    }

    // Проверка пароля
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
