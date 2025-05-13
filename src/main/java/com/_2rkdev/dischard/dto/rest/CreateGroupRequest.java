package com._2rkdev.dischard.dto.rest;

import java.util.List;

public record CreateGroupRequest(
        String name,
        String description,
        List<String> members
) {
}
