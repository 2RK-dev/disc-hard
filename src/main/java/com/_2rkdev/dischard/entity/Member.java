package com._2rkdev.dischard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "members")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    private UUID id;

    private String alias;

    private LocalDateTime joined;

    private String role;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "members")
    private List<Conversation> conversations;
}
