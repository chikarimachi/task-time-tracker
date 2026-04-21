package ru.test.tasktracker.service;

import org.springframework.stereotype.Service;
import ru.test.tasktracker.dto.auth.AuthTokenRequest;
import ru.test.tasktracker.dto.auth.AuthTokenResponse;
import ru.test.tasktracker.security.JwtService;

@Service
public class AuthService {

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthTokenResponse createToken(AuthTokenRequest request) {
        String token = jwtService.generateToken(request.getEmployeeId(), request.getFullName());
        return new AuthTokenResponse("Bearer", token);
    }
}
