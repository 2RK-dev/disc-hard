package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
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
import static com._2rkdev.dischard.assertion.OpenApiAssertions.assertValidOpenApiResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class UserControllerTest {
    String accessToken;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private static final String VALID_REGISTRATION_JSON =
            "{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}";

    @BeforeEach
    void setUp() throws Exception {
        accessToken = registerUserAndGetToken();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    /**
     * Helper method to register a user and get an access token
     */
    private String registerUserAndGetToken() throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REGISTRATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return JsonPath.read(registerResult.getResponse().getContentAsString(), "$.accessToken");
    }

    /**
     * Helper method to perform authenticated requests
     */
    private MvcResult performAuthenticatedRequest(String method, String endpoint, String content) throws Exception {
        var requestBuilder = switch (method.toLowerCase()) {
            case "patch" -> patch(endpoint);
            case "put" -> put(endpoint);
            case "get" -> get(endpoint);
            case "post" -> post(endpoint);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };

        requestBuilder.header("Authorization", "Bearer " + accessToken);

        if (content != null) {
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(content);
        }

        return mockMvc.perform(requestBuilder).andReturn();
    }

    /**
     * Helper method to register additional user and get user ID
     */
    private int registerAdditionalUser(String email) throws Exception {
        String registrationJson = "{\"email\":\"%s\",\"password\":\"pissword\", \"passwordConfirm\": \"pissword\", \"name\": \"%s\"}"
                .formatted(email, "Another");
        MvcResult registerResult = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(status().isOk())
                .andReturn();
        return JsonPath.read(registerResult.getResponse().getContentAsString(), "$.user.id");
    }

    @Nested
    class UpdateProfile {
        @Test
        void shouldBeBadRequest() throws Exception {
            String[] invalidRequests = {
                    "{\"name\":\"\"}",
                    "{}"
            };

            List<MvcResult> results = new ArrayList<>();
            for (String request : invalidRequests) {
                results.add(performAuthenticatedRequest("patch", "/profile", request));
            }

            Assertions.assertAll(results.stream()
                    .map(result -> (Executable) () -> {
                        assertThat(result.getResponse().getStatus()).isEqualTo(400);
                        assertValidOpenApi(result);
                    })
                    .toArray(Executable[]::new)
            );
        }

        @Test
        void shouldBeOk() throws Exception {
            String[] validRequests = {
                    "{\"name\":\"Naruto\"}",
                    "{\"name\":\"Naruto\", \"age\": 100, \"gender\": \"Male\", \"email\": \"<EMAIL>\"}"
            };

            List<MvcResult> results = new ArrayList<>();
            for (String request : validRequests) {
                results.add(performAuthenticatedRequest("patch", "/profile", request));
            }

            Assertions.assertAll(results.stream()
                    .map(result -> (Executable) () -> {
                        assertThat(result.getResponse().getStatus()).isEqualTo(200);
                        assertValidOpenApiResponse(result);
                    })
                    .toArray(Executable[]::new)
            );
        }
    }

    @Nested
    class ChangePassword {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult result = performAuthenticatedRequest("put", "/password",
                    "{\"old\": \"password\", \"new\":  \"newpass\", \"confirm\":  \"newpass\"}");
            assertThat(result.getResponse().getStatus()).isEqualTo(204);
            assertValidOpenApi(result);
        }

        @Test
        void shouldBeBadRequest() throws Exception {
            String[] invalidRequests = {
                    "{\"old\": \"\", \"new\":  \"newpass\", \"confirm\":  \"wrongpass\"}",
                    "{\"old\": \"password\", \"new\":  \"\", \"confirm\":  \"wrongpass\"}",
                    "{\"old\": \"password\", \"new\":  \"newpass\", \"confirm\":  \"\"}"
            };

            List<MvcResult> results = new ArrayList<>();
            for (String request : invalidRequests) {
                results.add(performAuthenticatedRequest("put", "/password", request));
            }

            Assertions.assertAll(results.stream()
                    .map(result -> (Executable) () -> {
                        assertThat(result.getResponse().getStatus()).isEqualTo(400);
                        assertValidOpenApi(result);
                    })
                    .toArray(Executable[]::new)
            );
        }

        @Test
        void shouldBeUnauthorized() throws Exception {
            MvcResult result = performAuthenticatedRequest("put", "/password",
                    "{\"old\": \"nottheold\", \"new\":  \"newpass\", \"confirm\":  \"newpass\"}");
            assertThat(result.getResponse().getStatus()).isEqualTo(401);
            assertValidOpenApi(result);
        }
    }

    @Nested
    class ListUsers {
        @Test
        void shouldBeOk() throws Exception {
            registerAdditionalUser("another@mail.com");
            MvcResult result = performAuthenticatedRequest("get", "/users", null);
            assertThat(result.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(result);
        }
    }

    @Nested
    class GetUserInfo {
        @Test
        void shouldBeOk() throws Exception {
            int userId = registerAdditionalUser("anotherone@mail.com");
            MvcResult result = performAuthenticatedRequest("get", "/users/" + userId, null);
            assertThat(result.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(result);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            MvcResult result = performAuthenticatedRequest("get", "/users/0", null);
            assertThat(result.getResponse().getStatus()).isEqualTo(404);
            assertValidOpenApi(result);
        }
    }
}