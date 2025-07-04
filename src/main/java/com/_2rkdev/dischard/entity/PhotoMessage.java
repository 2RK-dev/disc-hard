package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DiscriminatorValue("PHOTO")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PhotoMessage extends Message{
    private String photoName;
}
