package com.kirana.register.service;

import com.kirana.register.exception.CustomException;
import com.kirana.register.model.User;
import com.kirana.register.repository.UserRepository;
import com.kirana.register.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        // Validate email
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            throw new CustomException("Invalid email format", "INVALID_EMAIL_FORMAT");
        }

        // Validate password
        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            throw new CustomException("Invalid password format", "INVALID_PASSWORD_FORMAT");
        }

        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("User with email {} is already registered", user.getEmail());
            throw new CustomException("This email is already registered", "USER_ALREADY_PRESENT");
        }

        // Check for valid role
        if (!user.getRole().equalsIgnoreCase("OWNER") && !user.getRole().equalsIgnoreCase("CUSTOMER")) {
            logger.error("Invalid role: {}. Only 'OWNER' or 'CUSTOMER' allowed.", user.getRole());
            throw new CustomException("Invalid role provided", "INVALID_ROLE");
        }

        // Hash password and set registration date
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole().toUpperCase());

        // Save the user
        userRepository.save(user);
        logger.info("User successfully registered with email: {}", user.getEmail());
        return "User registered successfully";
    }
}
