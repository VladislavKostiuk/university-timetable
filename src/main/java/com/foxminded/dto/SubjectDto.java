package com.foxminded.dto;

public record SubjectDto(
        Long id,
        CourseDto courseDto,
        TeacherDto teacherDto,
        GroupDto groupDto
) {}
