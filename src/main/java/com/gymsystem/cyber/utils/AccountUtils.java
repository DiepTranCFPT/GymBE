package com.gymsystem.cyber.utils;


import java.util.Random;
import java.util.regex.Pattern;

public class AccountUtils {

    /**
     * random 1 ma code voi 6 so tu 000001 - 999999
     *
     * @return {@link String} code random
     */
    public static String generateRandomNumberString() {
        return String.format("%06d", new Random().nextInt(100000));
    }

    /**
     * ktr email
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(regex, email);
    }

    /**
     * ktr sdt
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Regex kiểm tra số điện thoại (10-11 chữ số)
        String regex = "^[0-9]{10,11}$";
        return Pattern.matches(regex, phoneNumber);
    }
}
