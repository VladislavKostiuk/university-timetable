package com.foxminded.dto;

import java.util.ArrayList;
import java.util.List;

public record CourseDto(
     Long id,
     String name,
     String description,
     List<SubjectDto> subjectDtoList,
     List<StudentDto> studentDtoList,
     List<TeacherDto> teacherDtoList
) {
    public CourseDto(Long id, String name, String description) {
        this(id, name, description, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
