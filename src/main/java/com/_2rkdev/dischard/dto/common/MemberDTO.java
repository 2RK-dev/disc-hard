package com._2rkdev.dischard.dto.common;

public record MemberDTO(
        Long id,
        String alias,
        UserDTO user,
        String role
) {
}