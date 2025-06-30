package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.MemberDTO;

import java.util.List;

public record GroupDataResponseDTO(
        GroupInfoDTO info,
        List<MemberDTO> members
) {
}
