package com._2rkdev.dischard.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "message_type")
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member member;

    private LocalDateTime timestamp;
}
