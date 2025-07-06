package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.common.MemberDTO;
import com._2rkdev.dischard.entity.Member;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberMapper {

    private final UserMapper userMapper;

    public MemberMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public MemberDTO toDTO(Member member) {
        if (member == null) {
            return null;
        }

        return new MemberDTO(
                member.getId(),
                member.getAlias() != null ? member.getAlias() : member.getUser().getName(),
                userMapper.toUserDTO(member.getUser()),
                member.getRole()
        );
    }

    public List<MemberDTO> toDTOList(List<Member> members) {
        if (members == null) {
            return List.of();
        }

        return members.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}