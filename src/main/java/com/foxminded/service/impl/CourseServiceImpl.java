package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.CourseDto;
import com.foxminded.mapper.CourseMapper;
import com.foxminded.entity.Course;
import com.foxminded.repository.CourseRepository;
import com.foxminded.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final String entityName = "Course";
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public void addCourse(CourseDto courseDto) {

        if (courseRepository.findById(courseDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Course course = courseMapper.mapToCourse(courseDto);
        courseRepository.save(course);
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return courseMapper.mapToCourseDto(course);
    }

    @Override
    public CourseDto getCourseByName(String name) {
        Course course = courseRepository.findByName(name).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_NAME, entityName, name
                )));
        return courseMapper.mapToCourseDto(course);
    }

    @Override
    public void deleteCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));

        for (var student : course.getStudents()) {
            student.getCourses().remove(course);
        }

        for (var teacher : course.getTeachers()) {
            teacher.getCourses().remove(course);
        }

        courseRepository.deleteById(id);
    }

    @Override
    public void updateCourse(CourseDto courseDto) {
        if (courseRepository.findById(courseDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Course course = courseMapper.mapToCourse(courseDto);
        courseRepository.save(course);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> allCourses = courseRepository.findAll();
        return allCourses.stream().map(courseMapper::mapToCourseDto).toList();
    }
}
