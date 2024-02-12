package com.foxminded.dto;

import com.foxminded.enums.TimetableType;

import java.util.List;

public record TimetableDto(
        Long id,
        TimetableType timetableType,
        String qualifyingName,
        List<LessonDto> lessonDtoList
) {}
