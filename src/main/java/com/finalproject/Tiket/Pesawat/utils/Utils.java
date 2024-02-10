package com.finalproject.Tiket.Pesawat.utils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class Utils {
    public static Date getCurrentDateTimeAsDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static CompletableFuture<String> generateRandomPasswordAsync() {
        return CompletableFuture.supplyAsync(() -> {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
            SecureRandom random = new SecureRandom();
            StringBuilder password = new StringBuilder(12);

            for (int i = 0; i < 12; i++) {
                password.append(characters.charAt(random.nextInt(characters.length())));
            }

            return password.toString();
        });
    }

    public static String extractPublicId(String cloudinaryUrl) {
        String[] parts = cloudinaryUrl.split("/");
        return parts[parts.length - 2];
    }

    public static String generateBookingCode(long bookingId, String userId) {
        String userIdPrefix = userId.substring(0, Math.min(5, userId.length()));
        String bookingCode = "AERSWFT" + bookingId + userIdPrefix;
        return bookingCode.toUpperCase();
    }

}
