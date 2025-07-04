package com._2rkdev.dischard.dto.common;

import com._2rkdev.dischard.entity.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        String avatar,
        String status
) {
    @Contract("_ -> new")
    public static @NotNull UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreated().toLocalDateTime(),
                user.getAvatar(),
                user.getStatus()
        );
    }
}