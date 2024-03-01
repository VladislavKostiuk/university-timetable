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
    @Mapping(source = "subject.teacher.courses", target = "teacherDto.courseDtoList")
    @Mapping(source = "subject.lessons", target = "lessonDtoList")
    SubjectDto mapToSubjectDto(Subject subject);

    @Mapping(source = "subjectDto.courseDto", target = "course")
    @Mapping(source = "subjectDto.teacherDto", target = "teacher")
    @Mapping(source = "subjectDto.groupDto", target = "group")
    @Mapping(source = "subjectDto.teacherDto.courseDtoList", target = "teacher.courses")
    @Mapping(source = "subjectDto.lessonDtoList", target = "lessons")
    Subject mapToSubject(SubjectDto subjectDto);
}
