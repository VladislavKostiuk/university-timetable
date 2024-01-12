package com.foxminded.dto;

import java.util.List;

public record StudentDTO (
        Long id,
        String name,
        GroupDTO groupDTO,
        List<CourseDTO> courseDTOList
) {
    public StudentDTO(String name, GroupDTO groupDTO, List<CourseDTO> courseDTOList) {
        this(0L, name, groupDTO, courseDTOList);
    }
}
