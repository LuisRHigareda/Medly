package com.medical.userservice.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Map<String, Object> response = new HashMap<>();

        if ("admin@demo.com".equals(email) && "1234".equals(password)) {
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("userRole", "PATIENT");
            response.put("email", email);
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    @GetMapping("/users/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("service", "user-service");
        response.put("status", "running");
        return ResponseEntity.ok(response);
    }
}