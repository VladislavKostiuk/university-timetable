package com.foxminded.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record LessonDto(
        Long id,
        SubjectDto subjectDto,
        DayOfWeek day,
        LocalTime appointmentTime
) {}
