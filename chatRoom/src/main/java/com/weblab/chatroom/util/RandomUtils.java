package com.weblab.chatroom.util;

import java.security.SecureRandom;

public class RandomUtils {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String randomString(String baseString, int length) {
        StringBuilder sb = new StringBuilder();
        if (length < 1) {
            length = 1;
        }

        int baseLength = baseString.length();

        for(int i = 0; i < length; ++i) {
            int number = secureRandom.nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }

        return sb.toString();
    }

}
