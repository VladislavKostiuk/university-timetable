package com.foxminded.mapper;

import com.foxminded.dto.StudentDto;
import com.foxminded.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "student.group", target = "groupDto")
    @Mapping(source = "student.courses", target = "courseDtoList")
    StudentDto mapToStudentDto(Student student);

    @Mapping(source = "studentDto.groupDto", target = "group")
    @Mapping(source = "studentDto.courseDtoList", target = "courses")
    Student mapToStudent(StudentDto studentDto);
}
