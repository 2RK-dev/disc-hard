package com._2rkdev.dischard.repository;

import com._2rkdev.dischard.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}