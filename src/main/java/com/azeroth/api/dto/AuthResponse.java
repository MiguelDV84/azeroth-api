package com.azeroth.api.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String username,
        String email,
        String role
) {
}

