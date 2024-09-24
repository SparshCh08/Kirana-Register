package com.kirana.register.controller;

import com.kirana.register.model.User;
import com.kirana.register.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Check if the role is valid (OWNER or CUSTOMER)
        if (user.getRole().equalsIgnoreCase("OWNER") || user.getRole().equalsIgnoreCase("CUSTOMER")) {
            userRepository.save(user);
            return "User registered successfully";
        } else {
            return "Invalid role. Only 'OWNER' or 'CUSTOMER' allowed.";
        }
    }
}
