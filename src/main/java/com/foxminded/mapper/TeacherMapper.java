package com.foxminded.mapper;

import com.foxminded.dto.TeacherDto;
import com.foxminded.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(source = "teacher.teacherCourses", target = "courseDtoList")
    TeacherDto mapToTeacherDto(Teacher teacher);

    @Mapping(source = "teacherDto.courseDtoList", target = "teacherCourses")
    Teacher mapToTeacher(TeacherDto teacherDto);
}
