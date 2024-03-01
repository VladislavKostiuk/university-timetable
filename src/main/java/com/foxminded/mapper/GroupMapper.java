package com.foxminded.mapper;

import com.foxminded.dto.GroupDto;
import com.foxminded.entity.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(source = "group.subjects", target = "subjectDtoList")
    @Mapping(source = "group.students", target = "studentDtoList")
    GroupDto mapToGroupDto(Group group);

    @Mapping(source = "groupDto.subjectDtoList", target = "subjects")
    @Mapping(source = "groupDto.studentDtoList", target = "students")
    Group mapToGroup(GroupDto groupDto);
}
