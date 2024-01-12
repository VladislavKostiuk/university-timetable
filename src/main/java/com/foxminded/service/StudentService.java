package com.foxminded.service;


import com.foxminded.dto.LessonDTO;
import com.foxminded.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    void addStudent(StudentDTO studentDTO);
    StudentDTO getStudentById(Long id);
    void deleteStudentById(Long id);
    void updateStudent(StudentDTO studentDTO);
    List<StudentDTO> getAllStudents();
}
