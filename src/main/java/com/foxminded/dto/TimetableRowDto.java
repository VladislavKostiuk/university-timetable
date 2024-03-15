package com.foxminded.dto;

import java.time.LocalTime;

public record TimetableRowDto(
        LocalTime time,
        LessonDto mondayLesson,
        LessonDto tuesdayLesson,
        LessonDto wednesdayLesson,
        LessonDto thursdayLesson,
        LessonDto fridayLesson,
        LessonDto sundayLesson
) {}
