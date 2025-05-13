package com._2rkdev.dischard.dto.rest;

import java.util.List;

public record GroupInvitationRequest(
        List<String> userIds
) {
}
