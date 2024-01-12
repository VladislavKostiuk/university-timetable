package com.foxminded.service;

import com.foxminded.dto.SubjectDTO;
import com.foxminded.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {
    void addTeacher(TeacherDTO teacherDTO);
    TeacherDTO getTeacherById(Long id);
    void deleteTeacherById(Long id);
    void updateTeacher(TeacherDTO teacherDTO);
    List<TeacherDTO> getAllTeachers();
}
