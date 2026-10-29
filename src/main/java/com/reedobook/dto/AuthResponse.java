
package com.reedobook.dto;

public record AuthResponse(
        String token,
        String tokenType,
        Long id,
        String name,
        String email,
        String role
) {}
