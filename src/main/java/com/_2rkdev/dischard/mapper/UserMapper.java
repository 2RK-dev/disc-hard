package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.common.UserDTO;
import com._2rkdev.dischard.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public @NotNull UserDTO toUserDTO(@NotNull User user) {
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
