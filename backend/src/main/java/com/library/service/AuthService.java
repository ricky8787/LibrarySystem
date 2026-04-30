package com.library.service;

import com.library.common.ProcedureResult;
import com.library.dto.AuthRequest;
import com.library.dto.AuthResponse;
import com.library.model.User;
import com.library.repository.StoredProcedureRepository;
import com.library.repository.UserRepository;
import com.library.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final StoredProcedureRepository spRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, StoredProcedureRepository spRepository,
            PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.spRepository = spRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public String register(AuthRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        int result = spRepository.registerUser(request.getPhone(), encodedPassword, request.getName());
        if (result == ProcedureResult.FAIL.getCode()) {
            throw new RuntimeException("Phone number already exists");
        }
        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        log.info("Attempting login for phone: {}", request.getPhone());
        User user = userRepository.findByPhoneNumber(request.getPhone())
                .orElseThrow(() -> new RuntimeException("Invalid phone number or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid phone number or password");
        }

        // Update last login time
        try {
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);

            String token = tokenProvider.generateToken(user.getPhoneNumber());
            log.info("User {} logged in successfully", user.getPhoneNumber());

            return new AuthResponse(token, user.getPhoneNumber());

        } catch (Exception e) {
            log.error("Login process failed due to unexpected error for phone: {}", request.getPhone(), e);
            throw new RuntimeException("Internal server error during login");
        }
    }
}
