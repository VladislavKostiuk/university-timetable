package com.foxminded.dto;

import com.foxminded.enums.CourseName;

public record CourseDTO (
     Long id,
     CourseName name,
     String description
) {
    public CourseDTO (CourseName name, String description) {
        this(0L, name, description);
    }
}
