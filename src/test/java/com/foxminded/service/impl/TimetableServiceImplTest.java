package com.foxminded.service.impl;

import com.foxminded.dto.SubjectDTO;
import com.foxminded.dto.TimetableDTO;
import com.foxminded.mapper.TimetableMapper;
import com.foxminded.mapper.TimetableMapperImpl;
import com.foxminded.model.Subject;
import com.foxminded.model.Timetable;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TimetableMapperImpl.class
})
class TimetableServiceImplTest {

    private TimetableService timetableService;
    @Mock
    private TimetableRepository timetableRepository;
    @Autowired
    private TimetableMapper timetableMapper;

    @BeforeEach
    void init() {
        timetableService = new TimetableServiceImpl(timetableRepository, timetableMapper);
    }

    @Test
    void testAddTimetable_Success() {
        TimetableDTO testTimetableDTO = new TimetableDTO(new ArrayList<>());
        when(timetableRepository.findById(testTimetableDTO.id())).thenReturn(Optional.empty());
        timetableService.addTimetable(testTimetableDTO);
        verify(timetableRepository).save(any());
    }

    @Test
    void testAddTimetable_TimetableAlreadyExists() {
        TimetableDTO testTimetableDTO = new TimetableDTO(new ArrayList<>());
        Timetable testTimetable = timetableMapper.mapToTimetable(testTimetableDTO);
        when(timetableRepository.findById(testTimetableDTO.id())).thenReturn(Optional.of(testTimetable));
        assertThrows(IllegalStateException.class, () -> timetableService.addTimetable(testTimetableDTO));
    }

    @Test
    void testGetTimetableById_Success() {
        Long id = 1L;
        Timetable timetable = new Timetable();
        timetable.setId(id);

        when(timetableRepository.findById(id)).thenReturn(Optional.of(timetable));
        TimetableDTO expectedTimetableDTO = timetableMapper.mapToTimetableDTO(timetable);
        TimetableDTO actualTimetableDTO = timetableService.getTimetableById(id);
        assertEquals(expectedTimetableDTO, actualTimetableDTO);
        verify(timetableRepository).findById(id);
    }

    @Test
    void testGetTimetableById_TimetableWasNotFound() {
        when(timetableRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> timetableService.getTimetableById(anyLong()));
    }

    @Test
    void testUpdateTimetable_Success() {
        TimetableDTO testTimetableDTO = new TimetableDTO(new ArrayList<>());
        Timetable testTimetable = timetableMapper.mapToTimetable(testTimetableDTO);
        when(timetableRepository.findById(testTimetableDTO.id())).thenReturn(Optional.of(testTimetable));
        timetableService.updateTimetable(testTimetableDTO);
        verify(timetableRepository).save(any());
    }

    @Test
    void testUpdateTimetable_TimetableDoesNotExist() {
        TimetableDTO testTimetableDTO = new TimetableDTO(new ArrayList<>());
        when(timetableRepository.findById(testTimetableDTO.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> timetableService.updateTimetable(testTimetableDTO));
    }
}