package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.CourseDTO;
import com.foxminded.mapper.CourseMapper;
import com.foxminded.model.Course;
import com.foxminded.repository.CourseRepository;
import com.foxminded.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final String entityName = "Course";
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl (CourseMapper courseMapper,
                              CourseRepository courseRepository) {
        this.courseMapper = courseMapper;
        this.courseRepository = courseRepository;
    }

    @Override
    public void addCourse(CourseDTO courseDTO) {

        if (courseRepository.findById(courseDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Course course = courseMapper.mapToCourse(courseDTO);
        courseRepository.save(course);
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return courseMapper.mapToCourseDTO(course);
    }

    @Override
    public void deleteCourseById(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public void updateCourse(CourseDTO courseDTO) {
        if (courseRepository.findById(courseDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Course course = courseMapper.mapToCourse(courseDTO);
        courseRepository.save(course);
    }
}