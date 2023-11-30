package com.foxminded.service;

import com.foxminded.dto.TeacherDTO;

public interface TeacherService {
    void addTeacher(TeacherDTO teacherDTO);
    TeacherDTO getTeacherById(Long id);
    void deleteTeacherById(Long id);
    void updateTeacher(TeacherDTO teacherDTO);
}
