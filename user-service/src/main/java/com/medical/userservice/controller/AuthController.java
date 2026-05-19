package com.medical.userservice.controller;

import com.medical.userservice.dto.LoginRequest;
import com.medical.userservice.dto.LoginResponse;
import com.medical.userservice.jwt.JWTUtil;
import com.medical.userservice.service.AuthService;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(false, "Invalid credentials", null, null, null)));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginHandler(@RequestBody LoginRequest request) {
        // Checks credentials in database
        Optional<LoginResponse> authCheck = authService.login(request);
        // If credentials are present, token is built and sent back
        if (authCheck.isPresent()) {
            LoginResponse response = authCheck.get();
            try {
                String generatedToken = JWTUtil.generateToken(
                        response.getUserId(),
                        response.getEmail(),
                        response.getUserRole()
                );
                return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("jwt-token", generatedToken));
            } catch (IllegalArgumentException | UnsupportedEncodingException ex) {
                return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Failure building the token"));
            }
            // If not, error is sent back
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

    @GetMapping("/users/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "user-service");
        response.put("status", "running");
        return ResponseEntity.ok(response);
    }
}