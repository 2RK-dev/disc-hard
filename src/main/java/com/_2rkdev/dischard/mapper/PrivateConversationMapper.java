package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.rest.PrivateConversationDTO;
import com._2rkdev.dischard.entity.Member;
import com._2rkdev.dischard.entity.PrivateConversation;
import org.springframework.stereotype.Component;

@Component
public class PrivateConversationMapper {

    private final MemberMapper memberMapper;

    public PrivateConversationMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public PrivateConversationDTO toDTO(PrivateConversation conversation, Long currentUserId) {
        if (conversation == null) {
            return null;
        }

        Member contactMember = conversation.getMembers().stream()
                .filter(member -> !member.getUser().getId().equals(currentUserId))
                .findFirst()
                .orElse(null);

        return new PrivateConversationDTO(
                conversation.getId(),
                memberMapper.toDTO(contactMember)
        );
    }
}