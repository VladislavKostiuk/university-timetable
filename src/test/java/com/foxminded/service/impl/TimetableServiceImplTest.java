package com.foxminded.service.impl;

import com.foxminded.dto.TimetableDto;
import com.foxminded.enums.TimetableType;
import com.foxminded.entity.Timetable;
import com.foxminded.mapper.*;
import com.foxminded.repository.TimetableRepository;
import com.foxminded.service.TimetableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
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
class TimetableServiceImplTest {

    private TimetableService timetableService;
    @Mock
    private TimetableRepository timetableRepository;
    @Autowired
    private SubjectMapper subjectMapper;
    private TimetableMapper timetableMapper;

    @BeforeEach
    void init() {
        timetableMapper = new TimetableMapperImpl(new LessonMapperImpl(subjectMapper));
        timetableService = new TimetableServiceImpl(timetableRepository, timetableMapper);
    }

    @Test
    void testAddTimetable_Success() {
        TimetableDto testTimetableDto = new TimetableDto(0L, TimetableType.STUDENT_TIMETABLE, "test group", new ArrayList<>());
        when(timetableRepository.findById(testTimetableDto.id())).thenReturn(Optional.empty());
        timetableService.addTimetable(testTimetableDto);
        verify(timetableRepository).save(any());
    }

    @Test
    void testAddTimetable_TimetableAlreadyExists() {
        TimetableDto testTimetableDto = new TimetableDto(0L, TimetableType.STUDENT_TIMETABLE, "test group", new ArrayList<>());
        Timetable testTimetable = timetableMapper.mapToTimetable(testTimetableDto);
        when(timetableRepository.findById(testTimetableDto.id())).thenReturn(Optional.of(testTimetable));
        assertThrows(IllegalStateException.class, () -> timetableService.addTimetable(testTimetableDto));
    }

    @Test
    void testGetTimetableById_Success() {
        Long id = 1L;
        Timetable timetable = new Timetable();
        timetable.setId(id);

        when(timetableRepository.findById(id)).thenReturn(Optional.of(timetable));
        TimetableDto expectedTimetableDto = timetableMapper.mapToTimetableDto(timetable);
        TimetableDto actualTimetableDto = timetableService.getTimetableById(id);
        assertEquals(expectedTimetableDto, actualTimetableDto);
        verify(timetableRepository).findById(id);
    }

    @Test
    void testGetTimetableById_TimetableWasNotFound() {
        when(timetableRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> timetableService.getTimetableById(anyLong()));
    }

    @Test
    void testUpdateTimetable_Success() {
        TimetableDto testTimetableDto = new TimetableDto(0L, TimetableType.STUDENT_TIMETABLE, "test group", new ArrayList<>());
        Timetable testTimetable = timetableMapper.mapToTimetable(testTimetableDto);
        when(timetableRepository.findById(testTimetableDto.id())).thenReturn(Optional.of(testTimetable));
        timetableService.updateTimetable(testTimetableDto);
        verify(timetableRepository).save(any());
    }

    @Test
    void testUpdateTimetable_TimetableDoesNotExist() {
        TimetableDto testTimetableDto = new TimetableDto(0L, TimetableType.STUDENT_TIMETABLE, "test group", new ArrayList<>());
        when(timetableRepository.findById(testTimetableDto.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> timetableService.updateTimetable(testTimetableDto));
    }
}

