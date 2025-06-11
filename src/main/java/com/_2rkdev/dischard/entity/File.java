package com._2rkdev.dischard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_messages")
@DiscriminatorValue("FILE")
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class File extends Message{
    private String fileName;
}
