package com.foxminded.mapper;

import com.foxminded.dto.TeacherDTO;
import com.foxminded.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "teacher.courses", target = "courseDTOList")
    TeacherDTO mapToTeacherDTO(Teacher teacher);

    @Mapping(source = "teacherDTO.courseDTOList", target = "courses")
    Teacher mapToTeacher(TeacherDTO teacherDTO);
}
