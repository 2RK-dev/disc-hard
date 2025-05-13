package com._2rkdev.dischard.dto.common;

public record Member(
        int id,
        String alias,
        User user,
        String role
) {
}