package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("GROUP")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class GroupConversation extends Conversation{
}
