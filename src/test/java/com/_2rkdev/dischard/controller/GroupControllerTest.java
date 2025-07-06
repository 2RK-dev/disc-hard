package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.entity.Member;
import com._2rkdev.dischard.entity.PrivateConversation;
import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.repository.ConversationRepository;
import com._2rkdev.dischard.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com._2rkdev.dischard.assertion.OpenApiAssertions.assertValidOpenApi;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class GroupControllerTest {
    String accessToken;
    int myUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private Long dmPartnerId;
    private Long inGroupId;
    private Long justAnUserId;
    @Autowired
    private ConversationRepository conversationRepository;

    @BeforeEach
    void setUp() throws Exception {
        User dmPartner = User.builder().name("Dm Partner").email("dmPartner@mail.com").password("kdjs").build();
        User inGroup = User.builder().name("In Group").email("inGroup@mail.com").password("kdjs").build();
        User justAnUser = User.builder().name("Just An User").email("justAnUser@mail.com").password("kdjs").build();
        userRepository.saveAll(List.of(dmPartner, inGroup, justAnUser));
        dmPartnerId = dmPartner.getId();
        inGroupId = inGroup.getId();
        justAnUserId = justAnUser.getId();
        MvcResult registerResult = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@mail.com\",\"password\":\"password\", \"passwordConfirm\": \"password\", \"name\": \"ahaha\"}")
                ).andExpect(status().isOk())
                .andReturn();
        accessToken = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.accessToken");
        myUserId = JsonPath.read(registerResult.getResponse().getContentAsString(), "$.user.id");
    }

    @AfterEach
    void tearDown() {
        conversationRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Helper method to create a group and return the group ID
     */
    private Long createTestGroup(List<Long> members) throws Exception {
        String membersJson = members.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ", "[", "]"));

        MvcResult createGroup = mockMvc.perform(post("/groups")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"test\", \"description\":  \"Desc\", \"members\": %s}".formatted(membersJson)))
                .andExpect(status().isCreated())
                .andReturn();

        assertValidOpenApi(createGroup);
        int id = JsonPath.read(createGroup.getResponse().getContentAsString(), "$.id");
        return (long) id;
    }

    /**
     * Helper method to create a group with a default member (inGroupId)
     */
    private Long createTestGroup() throws Exception {
        return createTestGroup(List.of(inGroupId));
    }

    /**
     * Helper method to create a private conversation for testing
     */
    private Long createPrivateConversation() {
        User dmPartner = userRepository.findById(dmPartnerId).orElseThrow();
        User myUser = userRepository.findById((long) myUserId).orElseThrow();
        PrivateConversation dm = new PrivateConversation();
        dm.addMembers(List.of(Member.builder().user(myUser).build(), Member.builder().user(dmPartner).build()));
        conversationRepository.save(dm);
        return dm.getId();
    }

    /**
     * Helper method to leave a group
     */
    private void leaveGroup(Long groupId) throws Exception {
        MvcResult leaveResult = mockMvc.perform(delete("/groups/" + groupId + "/members/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andReturn();
        assertValidOpenApi(leaveResult);
    }

    @Nested
    class CreateGroup {
        @Test
        void shouldBeCreated() throws Exception {
            String membersJson = Stream.of(dmPartnerId, inGroupId, justAnUserId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));

            MvcResult result = mockMvc.perform(post("/groups")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"test\", \"description\":  \"Desc\", \"members\": %s}".formatted(membersJson)))
                    .andExpect(status().isCreated())
                    .andReturn();
            assertValidOpenApi(result);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            long nonexistentId = 1L;
            while (List.of(dmPartnerId, inGroupId, justAnUserId).contains(nonexistentId))
                nonexistentId++;
            String membersJson = Stream.of(dmPartnerId, nonexistentId, justAnUserId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));

            MvcResult result = mockMvc.perform(post("/groups")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"test\", \"description\":  \"Desc\", \"members\": %s}".formatted(membersJson)))
                    .andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(result);
        }
    }

    @Nested
    class LeaveGroup {
        @Test
        void shouldBeNoContent() throws Exception {
            Long groupId = createTestGroup();

            MvcResult leaveAsAMember = mockMvc.perform(delete("/groups/" + groupId + "/members/me")
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isNoContent())
                    .andReturn();
            assertValidOpenApi(leaveAsAMember);

            MvcResult leaveAsANonMember = mockMvc.perform(delete("/groups/" + groupId + "/members/me")
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isNoContent())
                    .andReturn();
            assertValidOpenApi(leaveAsANonMember);
        }
    }

    @Nested
    class InviteToGroup {
        @Test
        void shouldBeNoContent() throws Exception {
            Long groupId = createTestGroup();

            MvcResult mvcResult = mockMvc.perform(post("/groups/" + groupId + "/members")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userIds\":  [%s,%s]}".formatted(dmPartnerId, justAnUserId)))
                    .andExpect(status().isNoContent())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeForbidden() throws Exception {
            Long groupId = createTestGroup();
            leaveGroup(groupId);

            MvcResult notAMemberAnymore = mockMvc.perform(post("/groups/" + groupId + "/members")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userIds\":  [%s,%s]}".formatted(dmPartnerId, justAnUserId)))
                    .andExpect(status().isForbidden())
                    .andReturn();
            assertValidOpenApi(notAMemberAnymore);
        }

        @Test
        void shouldBeNotFoundWhenInvitingToAPrivateConversation() throws Exception {
            Long dmId = createPrivateConversation();

            MvcResult mvcResult = mockMvc.perform(post("/groups/" + dmId + "/members")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userIds\":  [%s,%s]}".formatted(dmPartnerId, justAnUserId)))
                    .andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            MvcResult nonexistentGroup = mockMvc.perform(post("/groups/1/members")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userIds\":  [%s,%s]}".formatted(dmPartnerId, justAnUserId)))
                    .andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(nonexistentGroup);
        }
    }

    @Nested
    class GetGroupInfo {
        @Test
        void shouldBeCreated() throws Exception {
            Long groupId = createTestGroup();

            MvcResult mvcResult = mockMvc.perform(get("/groups/" + groupId)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeNotFoundWhenQueryingAPrivateConversation() throws Exception {
            Long dmId = createPrivateConversation();

            MvcResult mvcResult = mockMvc.perform(get("/groups/" + dmId)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeForbidden() throws Exception {
            Long groupId = createTestGroup();
            leaveGroup(groupId);

            MvcResult mvcResult = mockMvc.perform(get("/groups/" + groupId)
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isForbidden())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }

        @Test
        void shouldBeNotFound() throws Exception {
            MvcResult mvcResult = mockMvc.perform(get("/groups/1")
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isNotFound())
                    .andReturn();
            assertValidOpenApi(mvcResult);
        }
    }
}