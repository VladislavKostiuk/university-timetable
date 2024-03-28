package com.foxminded.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record LessonDto(
        Long id,
        SubjectDto subjectDto,
        DayOfWeek day,
        LocalTime appointmentTime
) {}
