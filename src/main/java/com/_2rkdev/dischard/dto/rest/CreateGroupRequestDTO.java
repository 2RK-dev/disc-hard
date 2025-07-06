package com._2rkdev.dischard.dto.rest;

import java.util.List;

public record CreateGroupRequestDTO(
        String name,
        String description,
        List<Long> members
) {
}
