package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("FILE")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class File extends Message{
    private String fileName;
}
