package com.foxminded.service;

import com.foxminded.dto.CourseDTO;

public interface CourseService {
    void addCourse(CourseDTO courseDTO);
    CourseDTO getCourseById(Long id);
    void deleteCourseById(Long id);
    void updateCourse(CourseDTO courseDTO);
}
