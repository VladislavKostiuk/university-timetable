package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.LessonDTO;
import com.foxminded.mapper.LessonMapper;
import com.foxminded.model.Lesson;
import com.foxminded.repository.LessonRepository;
import com.foxminded.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {
    private final String entityName = "Lesson";
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public void addLesson(LessonDTO lessonDTO) {
        if (lessonRepository.findById(lessonDTO.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Lesson lesson = lessonMapper.mapToLesson(lessonDTO);
        lessonRepository.save(lesson);
    }

    @Override
    public LessonDTO getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND, entityName, id
                )));
        return lessonMapper.mapToLessonDTO(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public void updateLesson(LessonDTO lessonDTO) {
        if (lessonRepository.findById(lessonDTO.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Lesson lesson = lessonMapper.mapToLesson(lessonDTO);
        lessonRepository.save(lesson);
    }
}
