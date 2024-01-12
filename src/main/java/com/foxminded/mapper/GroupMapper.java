package com.foxminded.mapper;

import com.foxminded.dto.GroupDTO;
import com.foxminded.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDTO mapToGroupDTO(Group group);

    Group mapToGroup(GroupDTO groupDTO);
}
