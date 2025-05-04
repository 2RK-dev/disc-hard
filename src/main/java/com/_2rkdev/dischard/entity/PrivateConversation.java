package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("PRIVATE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PrivateConversation extends Conversation{
}
