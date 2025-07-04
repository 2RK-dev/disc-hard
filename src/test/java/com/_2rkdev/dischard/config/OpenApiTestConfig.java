package com._2rkdev.dischard.config;

import com.atlassian.oai.validator.OpenApiInteractionValidator;

public class OpenApiTestConfig {

    private static final OpenApiInteractionValidator openApiInteractionValidator = OpenApiInteractionValidator
            .createForSpecificationUrl("/api/rest.yml")
            .withResolveRefs(true)
            .build();

    public static OpenApiInteractionValidator openApiValidator() {
        return openApiInteractionValidator;
    }
}