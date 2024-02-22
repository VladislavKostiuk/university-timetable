package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
import com.foxminded.dto.LessonDto;
import com.foxminded.mapper.LessonMapper;
import com.foxminded.entity.Lesson;
import com.foxminded.repository.LessonRepository;
import com.foxminded.service.LessonService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final String entityName = "Lesson";
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Override
    public void addLesson(LessonDto lessonDto) {
        if (lessonRepository.findById(lessonDto.id()).isPresent()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_ADDED, entityName
            ));
        }

        Lesson lesson = lessonMapper.mapToLesson(lessonDto);
        lessonRepository.save(lesson);
    }

    @Override
    public LessonDto getLessonById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format(
                        ErrorMessages.ENTITY_WAS_NOT_FOUND_BY_ID, entityName, id
                )));
        return lessonMapper.mapToLessonDto(lesson);
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public void updateLesson(LessonDto lessonDto) {
        if (lessonRepository.findById(lessonDto.id()).isEmpty()) {
            throw new IllegalStateException(String.format(
                    ErrorMessages.ENTITY_CAN_NOT_BE_UPDATED, entityName
            ));
        }

        Lesson lesson = lessonMapper.mapToLesson(lessonDto);
        lessonRepository.save(lesson);
    }

    @Override
    public List<LessonDto> getAllLessons() {
        List<Lesson> allLessons = lessonRepository.findAll();
        return allLessons.stream().map(lessonMapper::mapToLessonDto).toList();
    }
}
