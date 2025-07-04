package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.config.OpenApiTestConfig;
import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.repository.UserRepository;
import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.mockmvc.MockMvcRequest;
import com.atlassian.oai.validator.mockmvc.MockMvcResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class AuthControllerTest {
    private final OpenApiInteractionValidator validator = OpenApiTestConfig.openApiValidator();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    private void assertValidOpenApi(@NotNull MvcResult mvcResult) {
        ValidationReport report = validator.validate(MockMvcRequest.of(mvcResult.getRequest()), MockMvcResponse.of(mvcResult.getResponse()));
        System.out.println(report);
        assertThat(report.hasErrors()).isFalse();
    }

    @Nested
    class RegisterUser {
        @Test
        void shouldBeBadRequest() throws Exception {
            MvcResult mvcResult = mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\"}")
                    ).andExpect(status().isBadRequest())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeConflict() throws Exception {
            userRepository.save(User.builder()
                    .email("user@mail.com")
                    .password("ezeee")
                    .name("user")
                    .build()
            );
            MvcResult mvcResult = mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                    ).andExpect(status().isConflict())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeOk() throws Exception {
            MvcResult mvcResult = mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                    ).andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }
    }

    @Nested
    class LoginUser {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult registerResult = mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                    ).andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(registerResult);
            MvcResult loginResult = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\"}")
                    ).andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(loginResult);
        }

        @Test
        void shouldBeUnauthorized() throws Exception {
            MvcResult loginResult = mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\"}")
                    ).andExpect(status().isUnauthorized())
                    .andReturn();
            assertValidOpenApi(loginResult);
        }
    }
}