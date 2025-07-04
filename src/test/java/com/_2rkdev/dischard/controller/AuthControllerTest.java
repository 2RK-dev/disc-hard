package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com._2rkdev.dischard.assertion.OpenApiAssertions.assertValidOpenApi;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
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
            MvcResult registerResult = mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                    ).andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(registerResult);
            MvcResult loginUnregistered = mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"notuser@mail.com\",\"password\":\"password\"}")
            ).andReturn();

            MvcResult loginWithoutEmail = mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"password\":\"password\"}")
            ).andReturn();

            MvcResult loginWithoutPassword = mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"notuser@mail.com\",\"password\":\"password\"}")
            ).andReturn();

            MvcResult loginWithEmptyBody = mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            ).andReturn();

            MvcResult loginWithoutBody = mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"notuser@mail.com\",\"password\":\"password\"}")
            ).andReturn();

            Assertions.assertAll(
                    () -> assertThat(loginUnregistered.getResponse().getStatus()).isEqualTo(401),
                    () -> assertValidOpenApi(loginUnregistered),
                    () -> assertThat(loginUnregistered.getResponse().getStatus()).isEqualTo(401),
                    () -> assertThat(loginWithoutEmail.getResponse().getStatus()).isEqualTo(401),
                    () -> assertThat(loginWithoutPassword.getResponse().getStatus()).isEqualTo(401),
                    () -> assertThat(loginWithEmptyBody.getResponse().getStatus()).isEqualTo(401),
                    () -> assertThat(loginWithoutBody.getResponse().getStatus()).isEqualTo(401)
            );
        }
    }
}