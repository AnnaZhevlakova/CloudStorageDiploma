package com.example.CloudStorageDiploma.services;

import java.util.Base64;

public class Base64Example {

    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base64ToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
}
