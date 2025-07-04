package com._2rkdev.dischard.assertion;

import com._2rkdev.dischard.config.OpenApiTestConfig;
import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.mockmvc.MockMvcRequest;
import com.atlassian.oai.validator.mockmvc.MockMvcResponse;
import com.atlassian.oai.validator.model.Request.Method;
import com.atlassian.oai.validator.report.ValidationReport;
import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiAssertions {
    private final static OpenApiInteractionValidator VALIDATOR = OpenApiTestConfig.openApiValidator();

    public static void assertValidOpenApi(@NotNull MvcResult mvcResult) {
        ValidationReport report = VALIDATOR.validate(MockMvcRequest.of(mvcResult.getRequest()), MockMvcResponse.of(mvcResult.getResponse()));
        System.out.println(report);
        assertThat(report.hasErrors()).isFalse();
    }

    @SuppressWarnings("DataFlowIssue")
    public static void assertValidOpenApiResponse(@NotNull MvcResult mvcResult) {
        ValidationReport report = VALIDATOR.validateResponse(mvcResult.getRequest().getPathInfo(), Method.valueOf(mvcResult.getRequest().getMethod()), MockMvcResponse.of(mvcResult.getResponse()));
        System.out.println(report);
        assertThat(report.hasErrors()).isFalse();
    }
}
