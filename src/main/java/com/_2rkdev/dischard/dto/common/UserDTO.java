package com._2rkdev.dischard.dto.common;

import java.time.OffsetDateTime;

public record UserDTO(
        Long id,
        String name,
        String email,
        OffsetDateTime createdAt,
        String avatar,
        String status
) {
}