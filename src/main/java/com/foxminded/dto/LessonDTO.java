package com.foxminded.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record LessonDTO (
        Long id,
        SubjectDTO subjectDTO,
        DayOfWeek day,
        LocalTime appointmentTime
) {
    public LessonDTO(SubjectDTO subjectDTO, DayOfWeek day, LocalTime appointmentTime) {
        this(0L, subjectDTO, day, appointmentTime);
    }
}
