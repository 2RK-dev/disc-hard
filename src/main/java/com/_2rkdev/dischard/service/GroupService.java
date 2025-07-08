package com._2rkdev.dischard.service;

import com._2rkdev.dischard.dto.common.MemberDTO;
import com._2rkdev.dischard.dto.rest.CreateGroupRequestDTO;
import com._2rkdev.dischard.dto.rest.GroupDataResponseDTO;
import com._2rkdev.dischard.dto.rest.GroupInfoDTO;
import com._2rkdev.dischard.dto.rest.GroupInvitationRequestDTO;
import com._2rkdev.dischard.entity.GroupConversation;
import com._2rkdev.dischard.entity.Member;
import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.exception.GroupNotFoundException;
import com._2rkdev.dischard.exception.NotYourConversationException;
import com._2rkdev.dischard.exception.UserNotFoundException;
import com._2rkdev.dischard.mapper.GroupMapper;
import com._2rkdev.dischard.mapper.MemberMapper;
import com._2rkdev.dischard.repository.GroupConversationRepository;
import com._2rkdev.dischard.repository.MemberRepository;
import com._2rkdev.dischard.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupConversationRepository groupRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final GroupMapper groupMapper;
    private final MemberMapper memberMapper;

    public GroupService(GroupConversationRepository groupRepository,
                        UserRepository userRepository,
                        MemberRepository memberRepository,
                        GroupMapper groupMapper,
                        MemberMapper memberMapper) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.groupMapper = groupMapper;
        this.memberMapper = memberMapper;
    }

    @Transactional
    public GroupInfoDTO createGroup(CreateGroupRequestDTO request, String creatorEmail) {
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + creatorEmail));

        GroupConversation group = new GroupConversation();
        group.setGroupName(request.name());
        group.setGroupDescription(request.description());
        groupRepository.save(group);

        Member creatorMember = new Member();
        creatorMember.setUser(creator);
        creatorMember.setConversation(group);
        creatorMember.setRole("ADMIN");
        memberRepository.save(creatorMember);

        if (request.members() != null && !request.members().isEmpty()) {
            addMembersToGroup(group, request.members());
        }

        return groupMapper.toGroupInfoDTO(group);
    }

    @Transactional
    public void leaveGroup(Long groupId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userEmail));

        Optional<GroupConversation> group = groupRepository.findById(groupId);

        group.ifPresent(groupConversation -> memberRepository.deleteByUserIdAndConversationId(user.getId(), groupConversation.getId()));
    }

    @Transactional
    public void inviteToGroup(Long groupId, GroupInvitationRequestDTO request, String inviterEmail) {
        User inviter = userRepository.findByEmail(inviterEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + inviterEmail));

        GroupConversation group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupId));

        Member inviterMember = memberRepository.findByUserIdAndConversationId(inviter.getId(), group.getId())
                .orElseThrow(() -> new NotYourConversationException("You are not a member of this group"));

        if (!"ADMIN".equals(inviterMember.getRole())) {
            throw new IllegalArgumentException("Only admins can invite new members");
        }

        addMembersToGroup(group, request.userIds());
    }

    public GroupDataResponseDTO getGroupInfo(Long groupId, String userEmail) {
        GroupConversation group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + groupId));

        if (!memberRepository.existsByUser_EmailAndConversation_Id(userEmail, groupId)) {
            throw new NotYourConversationException("You are not a member of this group");
        }
        List<Member> members = memberRepository.findAllByConversationId(group.getId());
        List<MemberDTO> memberDTOs = memberMapper.toDTOList(members);

        return new GroupDataResponseDTO(
                groupMapper.toGroupInfoDTO(group),
                memberDTOs
        );
    }

    private void addMembersToGroup(GroupConversation group, @NotNull List<Long> userIds) {
        List<User> users = userIds.stream()
                .map(userRepository::findById)
                .map(user -> user.orElseThrow(() -> new UserNotFoundException("Not found")))
                .toList();

        for (User user : users) {
            if (memberRepository.findByUserIdAndConversationId(user.getId(), group.getId()).isPresent()) {
                continue;
            }

            Member member = new Member();
            member.setUser(user);
            member.setConversation(group);
            member.setRole("MEMBER");
            memberRepository.save(member);
        }
    }
}
