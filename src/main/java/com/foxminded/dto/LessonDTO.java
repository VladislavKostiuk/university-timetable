package com.foxminded.dto;

import java.time.LocalDateTime;

public record LessonDTO (
        Long id,
        SubjectDTO subjectDTO,
        LocalDateTime appointmentTime
) {
    public LessonDTO(SubjectDTO subjectDTO, LocalDateTime appointmentTime) {
        this(0L, subjectDTO, appointmentTime);
    }
}
