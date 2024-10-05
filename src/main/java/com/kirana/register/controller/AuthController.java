package com.kirana.register.controller;

import com.kirana.register.exception.CustomException;
import com.kirana.register.model.AuthRequest;
import com.kirana.register.service.CustomUserDetailsService;
import com.kirana.register.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public String createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            logger.info("Attempting to authenticate user with email: {}", authRequest.getEmail());

            // Authenticate user using Spring Security's AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Load user details using the custom UserDetailsService
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authRequest.getEmail());

            // Assuming your UserDetails implementation provides a way to get the user's role
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);
            logger.info("Token generated successfully for user: {}", userDetails.getUsername());

            return token;

        } catch (Exception e) {
            logger.error("Authentication failed for user with email: {}", authRequest.getEmail());

            // Throwing a custom exception to handle it in a centralized way
            throw new CustomException("Invalid email or password", "INVALID_CREDENTIALS");
        }
    }
}