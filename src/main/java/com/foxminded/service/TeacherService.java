package com.foxminded.service;

import com.foxminded.dto.TeacherDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    void addTeacher(TeacherDto teacherDto);
    TeacherDto getTeacherById(Long id);
    Optional<TeacherDto> getTeacherByName(String name);
    void deleteTeacherById(Long id);
    void updateTeacher(TeacherDto teacherDto);
    List<TeacherDto> getAllTeachers();
}
