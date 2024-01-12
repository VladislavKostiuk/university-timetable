package com.foxminded.mapper;

import com.foxminded.dto.StudentDTO;
import com.foxminded.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "student.group", target = "groupDTO")
    @Mapping(source = "student.courses", target = "courseDTOList")
    StudentDTO mapToStudentDTO(Student student);

    @Mapping(source = "studentDTO.groupDTO", target = "group")
    @Mapping(source = "studentDTO.courseDTOList", target = "courses")
    Student mapToStudent(StudentDTO studentDTO);
}
