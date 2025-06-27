package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.User;

public record LoginResponseDTO(
        String accessToken,
        User user
) {
}
