package com.foxminded.dto;

import java.util.ArrayList;
import java.util.List;

public record GroupDto(
        Long id,
        String name,
        List<SubjectDto> subjectDtoList,
        List<StudentDto> studentDtoList
) {
    public GroupDto(Long id, String name) {
        this(id, name, new ArrayList<>(), new ArrayList<>());
    }
}
