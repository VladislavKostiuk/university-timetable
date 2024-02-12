package com.foxminded.mapper;

import com.foxminded.dto.GroupDto;
import com.foxminded.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto mapToGroupDto(Group group);

    Group mapToGroup(GroupDto groupDto);
}
