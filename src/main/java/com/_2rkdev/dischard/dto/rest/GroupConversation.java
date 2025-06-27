package com._2rkdev.dischard.dto.rest;

import java.time.LocalDateTime;

public record GroupConversationDTO(
        Long id,
        String type,
        String name,
        String description,
        LocalDateTime createdAt
) {
}
