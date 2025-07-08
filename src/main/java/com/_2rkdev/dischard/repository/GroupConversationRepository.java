package com._2rkdev.dischard.repository;

import com._2rkdev.dischard.entity.GroupConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupConversationRepository extends JpaRepository<GroupConversation, Long> {
}
