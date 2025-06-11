package com._2rkdev.dischard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "text_messages")
@DiscriminatorValue("TEXT")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TextMessage extends Message{
    @Column(columnDefinition = "TEXT")
    private String textContent;
}
