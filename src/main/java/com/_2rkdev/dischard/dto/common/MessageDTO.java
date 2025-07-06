package com._2rkdev.dischard.dto.common;

import java.time.OffsetDateTime;

public record MessageDTO(
        Long id,
        String type,
        String textContent,
        OffsetDateTime timestamp,
        MemberDTO author
) {
}