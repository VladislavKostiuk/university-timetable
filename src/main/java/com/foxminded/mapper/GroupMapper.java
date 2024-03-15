package com.foxminded.mapper;

import com.foxminded.dto.GroupDto;
import com.foxminded.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto mapToGroupDto(Group group);

    Group mapToGroup(GroupDto groupDto);
}
