package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @BeforeEach
    void setUp() throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                ).andExpect(status().isOk())
                .andReturn();
        accessToken = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.accessToken");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    class UpdateProfile {
        @Test
        void shouldBeBadRequest() throws Exception {
            MvcResult blankNameRequest = mockMvc.perform(patch("/profile")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"\"}")
            ).andReturn();
            MvcResult noBodyRequest = mockMvc.perform(patch("/profile")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            ).andReturn();

            Assertions.assertAll(
                    () -> assertThat(blankNameRequest.getResponse().getStatus()).isEqualTo(400),
                    () -> assertValidOpenApi(blankNameRequest),
                    () -> assertThat(noBodyRequest.getResponse().getStatus()).isEqualTo(400),
                    () -> assertValidOpenApi(noBodyRequest)
            );
        }

        @Test
        void shouldBeOk() throws Exception {
            MvcResult goodRequest = mockMvc.perform(patch("/profile")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\"Naruto\"}")
                    ).andExpect(status().isOk())
                    .andReturn();
            MvcResult additionalFieldsRequest = mockMvc.perform(patch("/profile")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Naruto\", \"age\": 100, \"gender\": \"Male\", \"email\": \"<EMAIL>\"}")
            ).andReturn();
            Assertions.assertAll(
                    () -> assertThat(goodRequest.getResponse().getStatus()).isEqualTo(200),
                    () -> assertValidOpenApi(goodRequest),
                    () -> assertThat(additionalFieldsRequest.getResponse().getStatus()).isEqualTo(200),
                    () -> assertValidOpenApiResponse(additionalFieldsRequest)
            );
        }
    }

    @Nested
    class ChangePassword {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult goodRequest = mockMvc.perform(put("/password")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"old\": \"password\", \"new\":  \"newpass\", \"confirm\":  \"newpass\"}"))
                    .andExpect(status().isNoContent())
                    .andReturn();
            assertValidOpenApi(goodRequest);
        }

        @Test
        void shouldBeBadRequest() throws Exception {
            MvcResult noOldPassword = mockMvc.perform(put("/password")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"old\": \"\", \"new\":  \"newpass\", \"confirm\":  \"wrongpass\"}"))
                    .andReturn();
            MvcResult noNewPassword = mockMvc.perform(put("/password")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"old\": \"password\", \"new\":  \"\", \"confirm\":  \"wrongpass\"}"))
                    .andReturn();
            MvcResult noConfirmPassword = mockMvc.perform(put("/password")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"old\": \"password\", \"new\":  \"newpass\", \"confirm\":  \"\"}"))
                    .andReturn();
            Assertions.assertAll(
                    () -> assertThat(noOldPassword.getResponse().getStatus()).isEqualTo(400),
                    () -> assertValidOpenApi(noOldPassword),
                    () -> assertThat(noNewPassword.getResponse().getStatus()).isEqualTo(400),
                    () -> assertValidOpenApi(noNewPassword),
                    () -> assertThat(noConfirmPassword.getResponse().getStatus()).isEqualTo(400),
                    () -> assertValidOpenApi(noConfirmPassword)
            );
        }

        @Test
        void shouldBeUnauthorized() throws Exception {
            MvcResult result = mockMvc.perform(put("/password")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"old\": \"nottheold\", \"new\":  \"newpass\", \"confirm\":  \"newpass\"}"))
                    .andExpect(status().isUnauthorized())
                    .andReturn();
            assertValidOpenApi(result);
        }
    }

    @Nested
    class ListUsers {
        @Test
        void shouldBeOk() throws Exception {
            mockMvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"another@mail.com\",\"password\":\"pissword\", \"passwordConfirm\": \"pissword\", \"name\": \"Another\"}")
            ).andExpect(status().isOk());
            MvcResult result = mockMvc.perform(get("/users")
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(result);
        }
    }

    @Nested
    class GetUserInfo {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult registerResult = mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"anotherone@mail.com\",\"password\":\"pissword\", \"passwordConfirm\": \"pissword\", \"name\": \"Another\"}"))
                    .andExpect(status().isOk())
                    .andReturn();
            int userId = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.user.id");
            MvcResult result = mockMvc.perform(get("/users/" + userId)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(result);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            MvcResult result = mockMvc.perform(get("/users/0")
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(result);
        }
    }
}