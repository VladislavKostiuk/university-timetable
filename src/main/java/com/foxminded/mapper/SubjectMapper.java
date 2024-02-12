package com.foxminded.mapper;

import com.foxminded.dto.SubjectDto;
import com.foxminded.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mapping(source = "subject.course", target = "courseDto")
    @Mapping(source = "subject.teacher", target = "teacherDto")
    @Mapping(source = "subject.group", target = "groupDto")
    SubjectDto mapToSubjectDto(Subject subject);

    @Mapping(source = "subjectDto.courseDto", target = "course")
    @Mapping(source = "subjectDto.teacherDto", target = "teacher")
    @Mapping(source = "subjectDto.groupDto", target = "group")
    Subject mapToSubject(SubjectDto subjectDto);
}
