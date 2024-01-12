package com.foxminded.service;


import com.foxminded.dto.StudentDTO;

public interface StudentService {
    void addStudent(StudentDTO studentDTO);
    StudentDTO getStudentById(Long id);
    void deleteStudentById(Long id);
    void updateStudent(StudentDTO studentDTO);
}
