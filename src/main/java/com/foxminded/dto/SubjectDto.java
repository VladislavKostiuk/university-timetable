package com.foxminded.dto;

import java.util.List;

public record SubjectDto(
        Long id,
        CourseDto courseDto,
        TeacherDto teacherDto,
        GroupDto groupDto,
        List<LessonDto> lessonDtoList
) {}
