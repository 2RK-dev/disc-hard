package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.UserDTO;

public record LoginResponseDTO(
        String accessToken,
        UserDTO user
) {
}
