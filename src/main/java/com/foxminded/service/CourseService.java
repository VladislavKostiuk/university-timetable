package com.foxminded.service;

import com.foxminded.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDTO courseDTO);
    CourseDTO getCourseById(Long id);
    void deleteCourseById(Long id);
    void updateCourse(CourseDTO courseDTO);
    List<CourseDTO> getAllCourses();
}
