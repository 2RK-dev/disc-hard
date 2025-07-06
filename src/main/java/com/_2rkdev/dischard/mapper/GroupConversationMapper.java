package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.rest.GroupConversationDTO;
import com._2rkdev.dischard.entity.GroupConversation;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

/**
 * Mapper for GroupConversation entity to DTO
 */
@Component
public class GroupConversationMapper {

    public GroupConversationDTO toDTO(GroupConversation conversation) {
        if (conversation == null) {
            return null;
        }

        return new GroupConversationDTO(
                conversation.getId(),
                conversation.getGroupName(),
                conversation.getGroupDescription(),
                conversation.getCreated().toInstant().atOffset(ZoneOffset.UTC)
        );
    }
}