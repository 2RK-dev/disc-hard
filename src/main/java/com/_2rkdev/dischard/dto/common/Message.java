package com._2rkdev.dischard.dto.common;

import java.time.LocalDateTime;

public record Message(
        Long id,
        String type,
        String textContent,
        LocalDateTime timestamp,
        Member author
) {
}