package com.foxminded.mapper;

import com.foxminded.dto.StudentDTO;
import com.foxminded.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDTO mapToStudentDTO(Student student);
    Student mapToStudent(StudentDTO studentDTO);
}
