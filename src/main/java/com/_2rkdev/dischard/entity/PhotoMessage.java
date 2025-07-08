package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("PHOTO")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
public class PhotoMessage extends Message{
    private String photoName;
}
