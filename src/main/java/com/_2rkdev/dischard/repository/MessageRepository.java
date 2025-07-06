package com._2rkdev.dischard.repository;

import com._2rkdev.dischard.entity.Conversation;
import com._2rkdev.dischard.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> getMessagesByConversation(Conversation conversation, Pageable pageable);
}