package com._2rkdev.dischard.repository;

import com._2rkdev.dischard.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN FETCH c.members WHERE c.id = :id")
    Optional<Conversation> findByIdWithMembers(@Param("id") Long id);
}