package com.foxminded.service;

import com.foxminded.dto.LessonDTO;

public interface LessonService {
    void addLesson(LessonDTO lessonDTO);
    LessonDTO getLessonById(Long id);
    void deleteLessonById(Long id);
    void updateLesson(LessonDTO lessonDTO);
}
