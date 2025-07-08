package com._2rkdev.dischard.repository;

import com._2rkdev.dischard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByConversationId(Long conversationId);

    Optional<Member> findByUserIdAndConversationId(Long userId, Long conversationId);

    void deleteByUserIdAndConversationId(Long userId, Long conversationId);

    boolean existsByUser_EmailAndConversation_Id(String email, Long id);
}
