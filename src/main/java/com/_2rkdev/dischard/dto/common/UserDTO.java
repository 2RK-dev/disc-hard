package com._2rkdev.dischard.dto.common;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        String avatar,
        String status
) {
}