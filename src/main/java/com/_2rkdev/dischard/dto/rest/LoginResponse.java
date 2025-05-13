package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.User;

public record LoginResponse(
        String accessToken,
        User user
) {
}
