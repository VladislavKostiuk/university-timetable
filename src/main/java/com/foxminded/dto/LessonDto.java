package com.foxminded.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public record LessonDto(
        Long id,
        CourseDto courseDto,
        TeacherDto teacherDto,
        GroupDto groupDto,
        DayOfWeek day,
        LocalTime appointmentTime
) {}
