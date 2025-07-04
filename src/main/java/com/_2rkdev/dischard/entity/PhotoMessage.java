package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("PHOTO")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class PhotoMessage extends Message{
    private String photoName;
}
