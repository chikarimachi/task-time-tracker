package ru.test.tasktracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.test.tasktracker.dto.auth.AuthTokenRequest;
import ru.test.tasktracker.dto.auth.AuthTokenResponse;
import ru.test.tasktracker.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Получить JWT токен")
    @PostMapping("/token")
    public AuthTokenResponse createToken(@Valid @RequestBody AuthTokenRequest request) {
        return authService.createToken(request);
    }
}
