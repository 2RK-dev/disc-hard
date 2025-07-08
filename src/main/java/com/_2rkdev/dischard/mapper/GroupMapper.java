package com._2rkdev.dischard.mapper;

import com._2rkdev.dischard.dto.rest.GroupInfoDTO;
import com._2rkdev.dischard.entity.GroupConversation;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class GroupMapper {

    public GroupInfoDTO toGroupInfoDTO(GroupConversation group) {
        if (group == null) {
            return null;
        }

        return new GroupInfoDTO(
                group.getId(),
                "group",
                group.getGroupName(),
                group.getGroupDescription(),
                group.getCreated().toLocalDateTime().atOffset(ZoneOffset.UTC)
        );
    }
}
