package com._2rkdev.dischard.dto.rest;

import java.time.OffsetDateTime;

public record GroupInfoDTO(
        Long id,
        String type,
        String name,
        String description,
        OffsetDateTime createdAt
) {
}
