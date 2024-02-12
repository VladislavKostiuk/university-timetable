package com.foxminded.service.impl;

import com.foxminded.dto.TeacherDto;
import com.foxminded.mapper.TeacherMapper;
import com.foxminded.entity.Teacher;
import com.foxminded.mapper.TeacherMapperImpl;
import com.foxminded.repository.TeacherRepository;
import com.foxminded.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TeacherMapperImpl.class
})
class TeacherServiceImplTest {

    private TeacherService teacherService;
    @Mock
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherMapper teacherMapper;

    @BeforeEach
    void init() {
        teacherService = new TeacherServiceImpl(teacherRepository, teacherMapper);
    }

    @Test
    void testAddTeacher_Success() {
        TeacherDto testTeacherDto = new TeacherDto(0L, "name", "pass", new HashSet<>(), new ArrayList<>());
        when(teacherRepository.findById(testTeacherDto.id())).thenReturn(Optional.empty());
        teacherService.addTeacher(testTeacherDto);
        verify(teacherRepository).save(any());
    }

    @Test
    void testAddTeacher_TeacherAlreadyExists() {
        TeacherDto testTeacherDto = new TeacherDto(0L, "name", "pass", new HashSet<>(), new ArrayList<>());
        Teacher testTeacher = teacherMapper.mapToTeacher(testTeacherDto);
        when(teacherRepository.findById(testTeacherDto.id())).thenReturn(Optional.of(testTeacher));
        assertThrows(IllegalStateException.class, () -> teacherService.addTeacher(testTeacherDto));
    }

    @Test
    void testGetTeacherById_Success() {
        Long id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setName("name");

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));
        TeacherDto expectedTeacherDto = teacherMapper.mapToTeacherDto(teacher);
        TeacherDto actualTeacherDto = teacherService.getTeacherById(id);
        assertEquals(expectedTeacherDto, actualTeacherDto);
        verify(teacherRepository).findById(id);
    }

    @Test
    void testGetTeacherById_TeacherWasNotFound() {
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherById(anyLong()));
    }

    @Test
    void testGetTeacherByName_Success() {
        TeacherDto testTeacherDto = new TeacherDto(0L, "name", "pass", new HashSet<>(), new ArrayList<>());
        Teacher testTeacher = teacherMapper.mapToTeacher(testTeacherDto);
        when(teacherRepository.findByName("name")).thenReturn(Optional.of(testTeacher));
        assertEquals(testTeacherDto, teacherService.getTeacherByName("name"));
    }

    @Test
    void testGetTeacherByName_TeacherWithThisNameWasNotFound() {
        when(teacherRepository.findByName("name")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teacherService.getTeacherByName("name"));
    }

    @Test
    void testUpdateTeacher_Success() {
        TeacherDto testTeacherDto = new TeacherDto(0L, "name", "pass", new HashSet<>(), new ArrayList<>());
        Teacher testTeacher = teacherMapper.mapToTeacher(testTeacherDto);
        when(teacherRepository.findById(testTeacherDto.id())).thenReturn(Optional.of(testTeacher));
        teacherService.updateTeacher(testTeacherDto);
        verify(teacherRepository).save(any());
    }

    @Test
    void testUpdateTeacher_TeacherDoesNotExist() {
        TeacherDto testTeacherDto = new TeacherDto(0L, "name", "pass", new HashSet<>(), new ArrayList<>());
        when(teacherRepository.findById(testTeacherDto.id())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> teacherService.updateTeacher(testTeacherDto));
    }
}

