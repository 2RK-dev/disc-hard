package com._2rkdev.dischard.dto.rest;

import java.util.List;

public record GroupInvitationRequestDTO(
        List<Long> userIds
) {
}
