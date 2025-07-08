package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.rest.ConversationBaseDTO;
import com._2rkdev.dischard.entity.Conversation;
import com._2rkdev.dischard.entity.GroupConversation;
import com._2rkdev.dischard.entity.PrivateConversation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Polymorphic mapper for Conversation entities
 */
@Component
public class ConversationMapper {

    private final PrivateConversationMapper privateConversationMapper;
    private final GroupConversationMapper groupConversationMapper;

    public ConversationMapper(PrivateConversationMapper privateConversationMapper,
                              GroupConversationMapper groupConversationMapper) {
        this.privateConversationMapper = privateConversationMapper;
        this.groupConversationMapper = groupConversationMapper;
    }

    public ConversationBaseDTO toDTO(Conversation conversation, Long currentUserId) {
        return switch (conversation) {
            case null -> null;
            case PrivateConversation privateConversation ->
                    privateConversationMapper.toDTO(privateConversation, currentUserId);
            case GroupConversation groupConversation -> groupConversationMapper.toDTO(groupConversation);
            default ->
                    throw new IllegalArgumentException("Unknown conversation type: " + conversation.getClass().getSimpleName());
        };

    }

    public List<ConversationBaseDTO> toDTOList(List<Conversation> conversations, Long currentUserId) {
        if (conversations == null) {
            return List.of();
        }

        return conversations.stream()
                .map(conversation -> toDTO(conversation, currentUserId))
                .collect(Collectors.toList());
    }
}
