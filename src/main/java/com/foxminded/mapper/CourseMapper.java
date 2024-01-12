package com.foxminded.mapper;

import com.foxminded.dto.CourseDTO;
import com.foxminded.model.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDTO mapToCourseDTO(Course course);

    Course mapToCourse(CourseDTO courseDTO);
}