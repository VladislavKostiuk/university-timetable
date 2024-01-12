package com.foxminded.service;

import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.LessonDTO;

import java.util.List;

public interface LessonService {
    void addLesson(LessonDTO lessonDTO);
    LessonDTO getLessonById(Long id);
    void deleteLessonById(Long id);
    void updateLesson(LessonDTO lessonDTO);
    List<LessonDTO> getAllLessons();
}
