package com.foxminded.mapper;

import com.foxminded.dto.TeacherDTO;
import com.foxminded.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDTO mapToTeacherDTO(Teacher teacher);
    Teacher mapToTeacher(TeacherDTO teacherDTO);
}
