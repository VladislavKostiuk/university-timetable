package com.foxminded.dto;

import com.foxminded.enums.CourseName;

public record CourseDto(
     Long id,
     CourseName name,
     String description
) {}
