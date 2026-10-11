package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.dto.LoginRequestDTO;
import com.cts.Backend_ExamPlanner.dto.UserRegistrationDTO;
import com.cts.Backend_ExamPlanner.entities.User;
import com.cts.Backend_ExamPlanner.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.allowed-origin}")
@Validated
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret:ParvathiArunKanhaiyaRuthvikBhattuPranavaShivani}")
    private String secretKey;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole().toUpperCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .map(user -> {
                    // 1. Validate Password
                    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }

                    // 2. Validate Role
                    if (!user.getRole().equalsIgnoreCase(loginRequest.getRole())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("Access Denied: Incorrect role selected");
                    }

                    // 3. Generate JWT Token
                    // .signWith(Key, Algorithm) structure updated for jjwt 0.11.5
                    String token = Jwts.builder()
                            .setSubject(user.getEmail())
                            .claim("role", user.getRole())
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                            .compact();

                    // 4. Return user details and token
                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("role", user.getRole().toUpperCase());
                    response.put("name", user.getName());
                    response.put("email", user.getEmail());

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"));
    }
}