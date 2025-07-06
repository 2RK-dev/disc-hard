package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.entity.*;
import com._2rkdev.dischard.repository.ConversationRepository;
import com._2rkdev.dischard.repository.MessageRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com._2rkdev.dischard.assertion.OpenApiAssertions.assertValidOpenApi;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class ConversationControllerTest {
    String accessToken;
    Long dmId;
    Long groupId;
    Long groupWithoutUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() throws Exception {
        User dmPartner = User.builder().name("Dm Partner").email("dmPartner@mail.com").password("kdjs").build();
        User inGroup = User.builder().name("In Group").email("inGroup@mail.com").password("kdjs").build();
        User justAnUser = User.builder().name("Just An User").email("justAnUser@mail.com").password("kdjs").build();
        userRepository.saveAll(List.of(dmPartner, inGroup, justAnUser));
        MvcResult registerResult = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                ).andExpect(status().isOk())
                .andReturn();
        accessToken = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.accessToken");
        int userId = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.user.id");
        User user = userRepository.findById((long) userId).orElseThrow();

        PrivateConversation dm = new PrivateConversation();
        dm.addMembers(List.of(Member.builder().user(user).build(), Member.builder().user(dmPartner).build()));
        conversationRepository.save(dm);
        dmId = dm.getId();
        GroupConversation group = new GroupConversation();
        group.addMembers(List.of(
                Member.builder().user(user).role("owner").build(),
                Member.builder().user(inGroup).role("admin").build(),
                Member.builder().user(dmPartner).role("member").build())
        );
        conversationRepository.save(group);
        groupId = group.getId();
        GroupConversation groupWithoutUser = new GroupConversation();
        group.addMembers(List.of(
                Member.builder().user(inGroup).role("owner").build(),
                Member.builder().user(dmPartner).role("member").build())
        );
        conversationRepository.save(groupWithoutUser);
        groupWithoutUserId = groupWithoutUser.getId();
        messageRepository.saveAll(List.of(
                TextMessage.builder()
                        .member(dm.getMembers().getFirst())
                        .conversation(dm)
                        .timestamp(LocalDateTime.now())
                        .textContent("Hello World!")
                        .build(),
                TextMessage.builder()
                        .member(dm.getMembers().get(1))
                        .conversation(dm)
                        .timestamp(LocalDateTime.now())
                        .textContent("Hello World! 2")
                        .build(),
                TextMessage.builder()
                        .member(dm.getMembers().getFirst())
                        .conversation(dm)
                        .timestamp(LocalDateTime.now())
                        .textContent("Hello World! 3")
                        .build()
        ));
        messageRepository.saveAll(List.of(
                TextMessage.builder()
                        .member(group.getMembers().getFirst())
                        .conversation(group)
                        .timestamp(LocalDateTime.now())
                        .textContent("Hello World! 4")
                        .build(),
                TextMessage.builder()
                        .member(group.getMembers().get(1))
                        .conversation(group)
                        .timestamp(LocalDateTime.now())
                        .textContent("Hello World! 5")
                        .build(),
                TextMessage.builder()
                        .member(group.getMembers().get(2))
                        .conversation(group)
                        .timestamp(LocalDateTime.now())
                        .textContent("Hello World! 6")
                        .build()
        ));
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    class GetConversations {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult goodRequest = mockMvc.perform(get("/conversations")
                            .header("Authorization", "Bearer " + accessToken)
                    ).andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(goodRequest);
        }
    }

    @Nested
    class GetConversationMessages {
        @TestFactory
        Stream<DynamicTest> shouldBeOk() {
            Map<String, Long> map = Map.of(
                    "DM", dmId,
                    "Group withIn", groupId
            );
            return map.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
                MvcResult goodRequest = mockMvc.perform(get("/conversations/" + entry.getValue() + "/messages")
                                .header("Authorization", "Bearer " + accessToken)
                        ).andExpect(status().isOk())
                        .andReturn();
                assertValidOpenApi(goodRequest);
            }));
        }

        @Test
        void shouldBeForbidden() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/conversations/" + groupWithoutUserId + "/messages")
                            .header("Authorization", "Bearer " + accessToken)
                    ).andExpect(status().isForbidden())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            long id = 1L;
            while (List.of(dmId, groupId, groupWithoutUserId).contains(id)) id++;
            MvcResult mvcResult = mockMvc.perform(get("/conversations/" + id + "/messages")
                            .header("Authorization", "Bearer " + accessToken)
                    ).andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }
    }

    @Nested
    class GetConversationMembers {
        @TestFactory
        Stream<DynamicTest> shouldBeOk() {
            Map<String, Long> map = Map.of(
                    "DM", dmId,
                    "Group withIn", groupId
            );
            return map.entrySet().stream().map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
                MvcResult goodRequest = mockMvc.perform(get("/conversations/" + entry.getValue() + "/members")
                                .header("Authorization", "Bearer " + accessToken)
                        ).andExpect(status().isOk())
                        .andReturn();
                assertValidOpenApi(goodRequest);
            }));
        }

        @Test
        void shouldBeForbidden() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/conversations/" + groupWithoutUserId + "/members")
                            .header("Authorization", "Bearer " + accessToken)
                    ).andExpect(status().isForbidden())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            long id = 1L;
            while (List.of(dmId, groupId, groupWithoutUserId).contains(id)) id++;
            MvcResult mvcResult = mockMvc.perform(get("/conversations/" + id + "/members")
                            .header("Authorization", "Bearer " + accessToken)
                    ).andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }
    }
}