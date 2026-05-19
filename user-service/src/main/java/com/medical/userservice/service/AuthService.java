package com.medical.userservice.service;

import com.medical.userservice.dto.LoginRequest;
import com.medical.userservice.dto.LoginResponse;
import com.medical.userservice.model.User;
import com.medical.userservice.repository.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<LoginResponse> login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            return Optional.empty();
        }

        String email = request.getEmail().trim();
        String password = request.getPassword();

        if (email.isBlank() || password.isBlank()) {
            return Optional.empty();
        }

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            return Optional.empty();
        }

        User authenticatedUser = user.get();
        return Optional.of(new LoginResponse(
                true,
                "Login successful",
                authenticatedUser.getId(),
                authenticatedUser.getEmail(),
                authenticatedUser.getUserType().name()
        ));
    }
}
