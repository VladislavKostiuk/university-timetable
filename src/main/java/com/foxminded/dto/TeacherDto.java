package com.foxminded.dto;

import com.foxminded.enums.Role;

import java.util.List;
import java.util.Set;

public record TeacherDto(
        Long id,
        String name,
        String password,
        Set<Role> roles,
        List<CourseDto> courseDtoList
) {}
