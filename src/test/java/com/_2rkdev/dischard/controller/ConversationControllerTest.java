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
import static org.assertj.core.api.Assertions.assertThat;
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

    private static final String VALID_REGISTRATION_JSON =
            "{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}";

    @BeforeEach
    void setUp() throws Exception {
        List<User> testUsers = createTestUsers();
        User user = registerMainUserAndGetAccessToken();
        createConversations(user, testUsers);
        createTestMessages();
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Helper method to create test users
     */
    private List<User> createTestUsers() {
        User dmPartner = User.builder().name("Dm Partner").email("dmPartner@mail.com").password("kdjs").build();
        User inGroup = User.builder().name("In Group").email("inGroup@mail.com").password("kdjs").build();
        User justAnUser = User.builder().name("Just An User").email("justAnUser@mail.com").password("kdjs").build();
        userRepository.saveAll(List.of(dmPartner, inGroup, justAnUser));
        return List.of(dmPartner, inGroup, justAnUser);
    }

    /**
     * Helper method to register the main user and get an access token
     */
    private User registerMainUserAndGetAccessToken() throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REGISTRATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        accessToken = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.accessToken");
        int userId = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.user.id");
        return userRepository.findById((long) userId).orElseThrow();
    }

    /**
     * Helper method to create conversations
     */
    private void createConversations(User user, List<User> testUsers) {
        User dmPartner = testUsers.get(0);
        User inGroup = testUsers.get(1);

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
        groupWithoutUser.addMembers(List.of(
                Member.builder().user(inGroup).role("owner").build(),
                Member.builder().user(dmPartner).role("member").build())
        );
        conversationRepository.save(groupWithoutUser);
        groupWithoutUserId = groupWithoutUser.getId();
    }

    /**
     * Helper method to create a text message
     */
    private TextMessage createTextMessage(Member member, Conversation conversation, String content) {
        return TextMessage.builder()
                .member(member)
                .conversation(conversation)
                .timestamp(LocalDateTime.now())
                .textContent(content)
                .build();
    }

    /**
     * Helper method to perform authenticated GET requests
     */
    private MvcResult performAuthenticatedGet(String endpoint) throws Exception {
        return mockMvc.perform(get(endpoint)
                        .header("Authorization", "Bearer " + accessToken))
                .andReturn();
    }

    /**
     * Helper method to find a non-existent ID
     */
    private Long findNonExistentId(List<Long> existingIds) {
        long id = 1L;
        while (existingIds.contains(id)) {
            id++;
        }
        return id;
    }

    /**
     * Helper method to create test messages
     */
    private void createTestMessages() {
        Conversation dm = conversationRepository.findByIdWithMembers(dmId).orElseThrow();
        Conversation group = conversationRepository.findByIdWithMembers(groupId).orElseThrow();

        List<TextMessage> dmMessages = List.of(
                createTextMessage(dm.getMembers().get(0), dm, "Hello World!"),
                createTextMessage(dm.getMembers().get(1), dm, "Hello World! 2"),
                createTextMessage(dm.getMembers().get(0), dm, "Hello World! 3")
        );

        List<TextMessage> groupMessages = List.of(
                createTextMessage(group.getMembers().get(0), group, "Hello World! 4"),
                createTextMessage(group.getMembers().get(1), group, "Hello World! 5"),
                createTextMessage(group.getMembers().get(2), group, "Hello World! 6")
        );

        messageRepository.saveAll(dmMessages);
        messageRepository.saveAll(groupMessages);
    }

    @Nested
    class GetConversations {
        @Test
        void shouldBeOk() throws Exception {
            MvcResult result = performAuthenticatedGet("/conversations");
            assertThat(result.getResponse().getStatus()).isEqualTo(200);
            assertValidOpenApi(result);
        }
    }

    @Nested
    class GetConversationMessages {
        @TestFactory
        Stream<DynamicTest> shouldBeOk() {
            Map<String, Long> conversationMap = Map.of(
                    "DM", dmId,
                    "Group withIn", groupId
            );
            return conversationMap.entrySet().stream()
                    .map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
                        MvcResult result = performAuthenticatedGet("/conversations/" + entry.getValue() + "/messages");
                        assertThat(result.getResponse().getStatus()).isEqualTo(200);
                        assertValidOpenApi(result);
                    }));
        }

        @Test
        void shouldBeForbidden() throws Exception {
            MvcResult result = performAuthenticatedGet("/conversations/" + groupWithoutUserId + "/messages");
            assertThat(result.getResponse().getStatus()).isEqualTo(403);
            assertValidOpenApi(result);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            Long nonExistentId = findNonExistentId(List.of(dmId, groupId, groupWithoutUserId));
            MvcResult result = performAuthenticatedGet("/conversations/" + nonExistentId + "/messages");
            assertThat(result.getResponse().getStatus()).isEqualTo(404);
            assertValidOpenApi(result);
        }
    }

    @Nested
    class GetConversationMembers {
        @TestFactory
        Stream<DynamicTest> shouldBeOk() {
            Map<String, Long> conversationMap = Map.of(
                    "DM", dmId,
                    "Group withIn", groupId
            );
            return conversationMap.entrySet().stream()
                    .map(entry -> DynamicTest.dynamicTest(entry.getKey(), () -> {
                        MvcResult result = performAuthenticatedGet("/conversations/" + entry.getValue() + "/members");
                        assertThat(result.getResponse().getStatus()).isEqualTo(200);
                        assertValidOpenApi(result);
                    }));
        }

        @Test
        void shouldBeForbidden() throws Exception {
            MvcResult result = performAuthenticatedGet("/conversations/" + groupWithoutUserId + "/members");
            assertThat(result.getResponse().getStatus()).isEqualTo(403);
            assertValidOpenApi(result);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            Long nonExistentId = findNonExistentId(List.of(dmId, groupId, groupWithoutUserId));
            MvcResult result = performAuthenticatedGet("/conversations/" + nonExistentId + "/members");
            assertThat(result.getResponse().getStatus()).isEqualTo(404);
            assertValidOpenApi(result);
        }
    }
}