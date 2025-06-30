package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.MessageDTO;
import com._2rkdev.dischard.dto.common.PageDTO;

import java.util.List;

public record MessagePagedResponseDTO(
        List<MessageDTO> messages,
        PageDTO page
) {
}
