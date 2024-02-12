package com.foxminded.service.impl;

import com.foxminded.dto.LessonDTO;
import com.foxminded.mapper.LessonMapper;
import com.foxminded.entity.Lesson;
import com.foxminded.mapper.LessonMapperImpl;
import com.foxminded.mapper.SubjectMapper;
import com.foxminded.mapper.SubjectMapperImpl;
import com.foxminded.repository.LessonRepository;
import com.foxminded.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SubjectMapperImpl.class
})
class LessonServiceImplTest {

    private LessonService lessonService;
    @Mock
    private LessonRepository lessonRepository;
    @Autowired
    private SubjectMapper subjectMapper;
    private LessonMapper lessonMapper;

    @BeforeEach
    void init() {
        lessonMapper = new LessonMapperImpl(subjectMapper);
        lessonService = new LessonServiceImpl(lessonRepository, lessonMapper);
    }

    @Test
    void testAddLesson_Success() {
        LessonDTO testLessonDTO = new LessonDTO(null, DayOfWeek.MONDAY, LocalTime.now());
        when(lessonRepository.findById(testLessonDTO.id())).thenReturn(Optional.empty());
        lessonService.addLesson(testLessonDTO);
        verify(lessonRepository).save(any());
    }

    @Test
    void testAddLesson_LessonAlreadyExists() {
        LessonDTO testLessonDTO = new LessonDTO(null, DayOfWeek.MONDAY, LocalTime.now());
        Lesson testLesson = lessonMapper.mapToLesson(testLessonDTO);
        when(lessonRepository.findById(testLessonDTO.id())).thenReturn(Optional.of(testLesson));
        assertThrows(IllegalStateException.class, () -> lessonService.addLesson(testLessonDTO));
    }

    @Test
    void testGetLessonById_Success() {
        Long id = 1L;
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setAppointmentTime(LocalTime.now());

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        LessonDTO expectedLessonDTO = lessonMapper.mapToLessonDTO(lesson);
        LessonDTO actualLessonDTO = lessonService.getLessonById(id);
        assertEquals(expectedLessonDTO, actualLessonDTO);
        verify(lessonRepository).findById(id);
    }

    @Test
    void testGetLessonById_LessonWasNotFound() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> lessonService.getLessonById(anyLong()));
    }

    @Test
    void testUpdateLesson_Success() {
        LessonDTO testLessonDTO = new LessonDTO(null, DayOfWeek.MONDAY, LocalTime.now());
        Lesson testLesson = lessonMapper.mapToLesson(testLessonDTO);
        when(lessonRepository.findById(testLessonDTO.id())).thenReturn(Optional.of(testLesson));
        lessonService.updateLesson(testLessonDTO);
        verify(lessonRepository).save(any());
    }

    @Test
    void testUpdateLesson_LessonDoesNotExist() {
        LessonDTO testLessonDTO = new LessonDTO(null, DayOfWeek.MONDAY, LocalTime.now());
        when(lessonRepository.findById(testLessonDTO.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> lessonService.updateLesson(testLessonDTO));
    }
}
