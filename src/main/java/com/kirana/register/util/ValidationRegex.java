package com.kirana.register.util;

public class ValidationRegex {

    // Regex for validating Gmail emails
    public static final String GMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

    // Regex for validating passwords (example: at least 8 characters, one uppercase, one lowercase, one digit, one special character)
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
}
