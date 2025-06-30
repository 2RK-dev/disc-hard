package com._2rkdev.dischard.dto.common;

import java.time.LocalDateTime;

public record MessageDTO(
        Long id,
        String type,
        String textContent,
        LocalDateTime timestamp,
        MemberDTO author
) {
}