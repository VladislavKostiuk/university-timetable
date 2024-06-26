package com.foxminded.mapper;

import com.foxminded.dto.CourseDto;
import com.foxminded.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto mapToCourseDto(Course course);

    Course mapToCourse(CourseDto courseDto);
}
