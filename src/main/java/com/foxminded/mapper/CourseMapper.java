package com.foxminded.mapper;

import com.foxminded.dto.CourseDto;
import com.foxminded.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto mapToCourseDto(Course course);

    Course mapToCourse(CourseDto courseDto);
}
