package com.foxminded.dto;

import com.foxminded.model.Course;

import java.util.List;

public record TeacherDTO (
        Long id,
        String name,
        List<CourseDTO> courseDTOList
) {
    public TeacherDTO(String name, List<CourseDTO> courseDTOList) {
        this(0L, name, courseDTOList);
    }
}
