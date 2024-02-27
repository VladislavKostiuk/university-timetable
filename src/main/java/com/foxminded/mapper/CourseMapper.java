package com.foxminded.mapper;

import com.foxminded.dto.CourseDto;
import com.foxminded.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "course.students", target = "studentDtoList")
    @Mapping(source = "course.teachers", target = "teacherDtoList")
    @Mapping(source = "course.subjects", target = "subjectDtoList")
    CourseDto mapToCourseDto(Course course);

    @Mapping(source = "courseDto.studentDtoList", target = "students")
    @Mapping(source = "courseDto.teacherDtoList", target = "teachers")
    @Mapping(source = "courseDto.subjectDtoList", target = "subjects")
    Course mapToCourse(CourseDto courseDto);
}
