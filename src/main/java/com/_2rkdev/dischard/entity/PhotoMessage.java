package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photo_messages")
@DiscriminatorValue("PHOTO")
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class PhotoMessage extends Message{
    private String photoName;
}
