package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.repository.UserRepository;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static com._2rkdev.dischard.assertion.OpenApiAssertions.assertValidOpenApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Language("JSON")
    private static final String VALID_REGISTRATION_JSON =
            "{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}";
    private static final String VALID_LOGIN_JSON =
            "{\"email\":\"user@mail.com\",\"password\":\"password\"}";

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    /**
     * Helper method to perform registration
     */
    private MvcResult performRegistration(@Language("JSON") String jsonContent) throws Exception {
        return mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();
    }

    /**
     * Helper method to perform login
     */
    private MvcResult performLogin(String jsonContent) throws Exception {
        return mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn();
    }

    @Nested
    class RegisterUser {
        @Test
        void shouldBeBadRequest() throws Exception {
            MvcResult mvcResult = performRegistration("{\"email\":\"user@mail.com\",\"password\":\"password\"}");
            assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeConflict() throws Exception {
            userRepository.save(User.builder()
                    .email("user@mail.com")
                    .password("ezeee")
                    .name("user")
                    .build());
            MvcResult mvcResult = performRegistration("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}");
            assertThat(mvcResult.getResponse().getStatus()).isEqualTo(409);
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeOk() throws Exception {
            MvcResult mvcResult = performRegistration(VALID_REGISTRATION_JSON);
            assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(mvcResult);
        }
    }

    @Nested
    class LoginUser {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult registerResult = performRegistration(VALID_REGISTRATION_JSON);
            assertThat(registerResult.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(registerResult);

            MvcResult loginResult = performLogin(VALID_LOGIN_JSON);
            assertThat(loginResult.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(loginResult);
        }

        @Test
        void shouldBeUnauthorized() throws Exception {
            MvcResult registerResult = performRegistration(VALID_REGISTRATION_JSON);
            assertThat(registerResult.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(registerResult);

            String[] invalidLoginRequests = {
                    "{\"email\":\"notuser@mail.com\",\"password\":\"password\"}",
                    "{\"password\":\"password\"}",
                    "{\"email\":\"user@mail.com\"}",
                    "{}",
                    "{\"email\":\"user@mail.com\",\"password\":\"wrongpassword\"}"
            };

            List<MvcResult> results = new ArrayList<>();
            for (String request : invalidLoginRequests) {
                results.add(performLogin(request));
            }
            Assertions.assertAll(results.stream()
                    .map(result -> (Executable) () -> {
                        assertThat(result.getResponse().getStatus()).isEqualTo(401);
                        assertValidOpenApi(result);
                    })
                    .toArray(Executable[]::new)
            );
        }
    }
}