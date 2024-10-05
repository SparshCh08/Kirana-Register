package com.kirana.register.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

    // Method to validate email format
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(ValidationRegex.GMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Method to validate password format
    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(ValidationRegex.PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
