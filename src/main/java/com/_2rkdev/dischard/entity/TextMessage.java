package com._2rkdev.dischard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("TEXT")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TextMessage extends Message{
    @Column(columnDefinition = "TEXT")
    private String textContent;
}
