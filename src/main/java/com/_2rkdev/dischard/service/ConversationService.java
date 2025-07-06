package com._2rkdev.dischard.service;

import com._2rkdev.dischard.dto.common.MemberDTO;
import com._2rkdev.dischard.dto.rest.ConversationBaseDTO;
import com._2rkdev.dischard.dto.rest.MessagePagedResponseDTO;
import com._2rkdev.dischard.entity.Conversation;
import com._2rkdev.dischard.entity.Member;
import com._2rkdev.dischard.entity.Message;
import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.exception.ConversationNotFoundException;
import com._2rkdev.dischard.exception.NotYourConversationException;
import com._2rkdev.dischard.exception.UserNotFoundException;
import com._2rkdev.dischard.mapper.ConversationMapper;
import com._2rkdev.dischard.mapper.MemberMapper;
import com._2rkdev.dischard.mapper.MessageMapper;
import com._2rkdev.dischard.repository.ConversationRepository;
import com._2rkdev.dischard.repository.MessageRepository;
import com._2rkdev.dischard.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final MemberMapper memberMapper;

    public ConversationService(UserRepository userRepository, ConversationMapper conversationMapper, MessageRepository messageRepository, ConversationRepository conversationRepository, MessageMapper messageMapper, MemberMapper memberMapper) {
        this.userRepository = userRepository;
        this.conversationMapper = conversationMapper;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.messageMapper = messageMapper;
        this.memberMapper = memberMapper;
    }

    public List<? extends ConversationBaseDTO> getConversations(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Conversation> conversations = user.getMemberships().stream().map(Member::getConversation).toList();
        return conversationMapper.toDTOList(conversations, user.getId());
    }

    public MessagePagedResponseDTO getConversationMessages(long conversationId, String userEmail, int page, int size) {
        Conversation convo = conversationRepository.findById(conversationId).orElseThrow(() -> new ConversationNotFoundException("Convo not found"));
        if (convo.getMembers().stream().noneMatch(member -> member.getUser().getEmail().equals(userEmail))) {
            throw new NotYourConversationException("Not a member");
        }
        Page<Message> messagePage = messageRepository.getMessagesByConversation(convo, PageRequest.of(page, size));
        return messageMapper.toDtoPage(messagePage);
    }

    public List<MemberDTO> getConversationMembers(long conversationId, String userEmail) {
        Conversation convo = conversationRepository.findById(conversationId).orElseThrow(() -> new ConversationNotFoundException("Convo not found"));
        if (convo.getMembers().stream().noneMatch(member -> member.getUser().getEmail().equals(userEmail))) {
            throw new NotYourConversationException("Not a member");
        }
        return memberMapper.toDTOList(convo.getMembers());
    }
}
