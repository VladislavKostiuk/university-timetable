package com.foxminded.dto;

import com.foxminded.enums.TimetableType;

import java.util.List;

public record TimetableDTO (
        Long id,
        TimetableType timetableType,
        String qualifyingName,
        List<LessonDTO> lessonDTOList
) {
    public TimetableDTO(TimetableType timetableType, String qualifyingName, List<LessonDTO> lessonDTOList) {
        this(0L, timetableType, qualifyingName, lessonDTOList);
    }
}
