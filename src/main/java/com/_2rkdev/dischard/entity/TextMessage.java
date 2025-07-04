package com._2rkdev.dischard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("TEXT")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class TextMessage extends Message{
    @Column(columnDefinition = "TEXT")
    private String textContent;
}
