package com._2rkdev.dischard.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private final String ressourceName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String ressourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s", fieldName, ressourceName, fieldValue));
        this.ressourceName = ressourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
