package com.foxminded.service;


import com.foxminded.dto.StudentDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    void addStudent(StudentDto studentDto);
    StudentDto getStudentById(Long id);
    Optional<StudentDto> getStudentByName(String name);
    void deleteStudentById(Long id);
    void updateStudent(StudentDto studentDto);
    List<StudentDto> getAllStudents();
}
