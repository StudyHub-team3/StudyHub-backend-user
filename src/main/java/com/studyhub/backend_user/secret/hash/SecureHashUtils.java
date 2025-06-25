package com.studyhub.backend_user.secret.hash;

import com.studyhub.backend_user.common.exception.BadParameter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecureHashUtils {
    private static final BCryptPasswordEncoder BCRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static String hash(String message) {
        if (message == null || message.isBlank()) {
            throw new BadParameter("문자열이 공백으로 이루어져 있습니다.");
        }

        return BCRYPT_PASSWORD_ENCODER.encode(message);
    }

    public static boolean match(String message, String hashedMessage) {
        if (message == null || message.isBlank()) {
            throw new BadParameter("문자열이 공백으로 이루어져 있습니다.");
        }

        if (hashedMessage == null || hashedMessage.isBlank()) {
            throw new BadParameter("문자열이 공백으로 이루어져 있습니다.");
        }

        return BCRYPT_PASSWORD_ENCODER.matches(message, hashedMessage);
    }
}
