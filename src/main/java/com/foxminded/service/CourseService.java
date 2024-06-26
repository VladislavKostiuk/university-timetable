package com.foxminded.service;

import com.foxminded.dto.CourseDto;

import java.util.List;

public interface CourseService {
    void addCourse(CourseDto courseDto);
    CourseDto getCourseById(Long id);
    CourseDto getCourseByName(String name);
    void deleteCourseById(Long id);
    void updateCourse(CourseDto courseDto);
    List<CourseDto> getAllCourses();
}
