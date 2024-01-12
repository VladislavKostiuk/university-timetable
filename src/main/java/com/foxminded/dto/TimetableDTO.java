package com.foxminded.dto;

import java.util.List;

public record TimetableDTO (
        Long id,
        List<LessonDTO> lessonDTOList
) {
    public TimetableDTO(List<LessonDTO> lessonDTOList) {
        this(0L, lessonDTOList);
    }
}
