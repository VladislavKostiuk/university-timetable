package com.foxminded.service;

import com.foxminded.dto.LessonDto;

import java.util.List;

public interface LessonService {
    void addLesson(LessonDto lessonDto);
    LessonDto getLessonById(Long id);
    void deleteLessonById(Long id);
    void updateLesson(LessonDto lessonDto);
    List<LessonDto> getAllLessons();
}
