package com.foxminded.service;


import com.foxminded.dto.StudentDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface StudentService {
    void addStudent(StudentDto studentDto);
    StudentDto getStudentById(Long id);
    StudentDto getStudentByName(String name);
    void deleteStudentById(Long id);
    void updateStudent(StudentDto studentDto);
    List<StudentDto> getAllStudents();
}
