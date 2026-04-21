package ru.test.tasktracker.dto.auth;

public record AuthTokenResponse(
        String tokenType,
        String accessToken
) {
}
