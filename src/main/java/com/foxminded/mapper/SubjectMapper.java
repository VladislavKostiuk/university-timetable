package com.foxminded.mapper;

import com.foxminded.dto.SubjectDTO;
import com.foxminded.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    @Mapping(source = "subject.course", target = "courseDTO")
    @Mapping(source = "subject.teacher", target = "teacherDTO")
    @Mapping(source = "subject.group", target = "groupDTO")
    SubjectDTO mapToSubjectDTO(Subject subject);

    @Mapping(source = "subjectDTO.courseDTO", target = "course")
    @Mapping(source = "subjectDTO.teacherDTO", target = "teacher")
    @Mapping(source = "subjectDTO.groupDTO", target = "group")
    Subject mapToSubject(SubjectDTO subjectDTO);
}
