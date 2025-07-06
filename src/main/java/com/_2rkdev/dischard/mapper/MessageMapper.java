package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.common.MessageDTO;
import com._2rkdev.dischard.dto.common.PageDTO;
import com._2rkdev.dischard.dto.rest.MessagePagedResponseDTO;
import com._2rkdev.dischard.entity.Message;
import com._2rkdev.dischard.entity.TextMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class MessageMapper {
    private final MemberMapper memberMapper;

    public MessageMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public MessageDTO toDto(Message message) {
        if (message instanceof TextMessage textMessage) {
            return new MessageDTO(
                    textMessage.getId(),
                    "text",
                    textMessage.getTextContent(),
                    textMessage.getTimestamp().atOffset(ZoneOffset.UTC),
                    memberMapper.toDTO(textMessage.getMember())
            );
        }
        throw new UnsupportedOperationException("No support for non text messages yet");
    }

    public MessagePagedResponseDTO toDtoPage(@NotNull Page<Message> messages) {
        return new MessagePagedResponseDTO(
                messages.map(this::toDto).toList(),
                new PageDTO(
                        messages.getSize(),
                        messages.getNumberOfElements(),
                        messages.getTotalPages(),
                        messages.getNumber()
                )
        );
    }
}
