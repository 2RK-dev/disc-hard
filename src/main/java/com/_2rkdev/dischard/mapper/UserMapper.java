package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.common.UserDTO;
import com._2rkdev.dischard.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;

@Component
public class UserMapper {
    public @NotNull UserDTO toUserDTO(@NotNull User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreated().toLocalDateTime().atOffset(ZoneOffset.UTC),
                user.getAvatar(),
                user.getStatus()
        );
    }

    public List<UserDTO> toUserDTOList(@NotNull List<User> users) {
        return users.stream().map(this::toUserDTO).toList();
    }
}
