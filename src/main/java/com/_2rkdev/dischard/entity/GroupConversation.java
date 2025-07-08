package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("GROUP")
@NoArgsConstructor
@Getter
@Setter
public class GroupConversation extends Conversation{
    private String groupName;
    private String groupDescription;
}
